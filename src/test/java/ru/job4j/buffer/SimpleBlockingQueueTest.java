package ru.job4j.buffer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import ru.job4j.buffer.SimpleBlockingQueue;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.IntStream;

class SimpleBlockingQueueTest {

  @Test
  void whenOneProducerAndOneConsumer() throws InterruptedException {
    SimpleBlockingQueue<String> queue = new SimpleBlockingQueue<>(10);
    List<String> messages = List.of("hello", "world", "bonjour", "le monde");

    Thread producer = new Thread(() -> messages.forEach(s -> {
      try {
        queue.offer(s);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        fail();
      }
    }));
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

  @Test
  void whenTwoProducersAndOneConsumerGetAll() throws InterruptedException {
    final CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();
    final SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);

    Thread producer1 = new Thread(
        () -> {
          IntStream.range(0, 5).forEach( i -> {
                try {
                  queue.offer(i);
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
              }
          );
        }
    );
    Thread producer2 = new Thread(
        () -> {
          IntStream.range(5, 10).forEach( i -> {
                try {
                  queue.offer(i);
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                }
              }
          );
        }
    );
    producer1.start();
    producer2.start();
    Thread consumer = new Thread(
        () -> {
          while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
            try {
              buffer.add(queue.poll());
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }
          }
        }
    );
    consumer.start();
    producer1.join();
    producer2.join();
    consumer.interrupt();
    consumer.join();
    assertThat(buffer).containsAll(IntStream.range(0, 10).boxed().toList());
  }

}