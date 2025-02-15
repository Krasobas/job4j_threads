package ru.job4j.cas;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class CASCountTest {

  @Test
  void whenSomeIncrementThenOk() throws InterruptedException {
    int times = 100;
    CASCount count = new CASCount();
    Runnable incrementTask = () -> {
      for (int i = 0; i < times; i++) {
        count.increment();
      }
    };

    List<Thread> threads = IntStream.range(0, times).boxed().map((i) -> new Thread(incrementTask)).toList();

    threads.forEach(Thread::start);
    threads.forEach(t -> {
      try {
        t.join();
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }
    });

    assertThat(count.get()).isEqualTo(times * times);
  }
}