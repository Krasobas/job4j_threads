package ru.job4j;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import java.util.List;

class SimpleBlockingQueueTest {

  @Test
  void whenOneProducerAndOneConsumer() throws InterruptedException {
    SimpleBlockingQueue<String> queue = new SimpleBlockingQueue<>();
    List<String> messages = List.of("hello", "world", "bonjour", "le monde");

    Thread producer = new Thread(() -> messages.forEach(queue::offer));
    Thread consumer = new Thread(() -> messages.forEach(s -> assertThat(s).isEqualTo(queue.poll())));
    consumer.start();
    producer.start();
    producer.join();
    consumer.join();
  }

}