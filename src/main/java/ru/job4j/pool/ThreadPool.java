package ru.job4j.pool;

import ru.job4j.buffer.SimpleBlockingQueue;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class ThreadPool {
  private final List<Thread> threads;
  private final SimpleBlockingQueue<Runnable> tasks;

  public ThreadPool(int queueSize) {
    this.threads = new LinkedList<>();
    this.tasks = new SimpleBlockingQueue<>(queueSize);
    init();
  }

  public void work(Runnable job) {
    try {
      tasks.offer(job);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void shutdown() {
    for (Thread thread : threads) {
      thread.interrupt();
    }
  }

  private void init() {
    for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
      Thread current = new Thread(() -> {
        while (!Thread.currentThread().isInterrupted()) {
          try {
            Optional.ofNullable(tasks.poll()).ifPresent(Runnable::run);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        }
      });
      current.start();
      threads.add(current);
    }
  }
}