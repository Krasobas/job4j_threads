package ru.job4j.buffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import ru.job4j.buffer.SimpleBlockingQueue;

import java.util.List;

class SimpleBlockingQueueTest {

  @Test
  void whenOneProducerAndOneConsumer() throws InterruptedException {
    SimpleBlockingQueue<String> queue = new SimpleBlockingQueue<>();
    List<String> messages = List.of("hello", "world", "bonjour", "le monde");

    Thread producer = new Thread(() -> messages.forEach(queue::offer));
    Thread consumer = new Thread(() -> messages.forEach(s -> {
      try {
        assertThat(s).isEqualTo(queue.poll());
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        fail();
      }
    }));
    consumer.start();
    producer.start();
    producer.join();
    consumer.join();
  }

}