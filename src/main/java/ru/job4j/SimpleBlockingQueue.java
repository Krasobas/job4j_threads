package ru.job4j;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {

  @GuardedBy("this")
  private final Queue<T> queue = new LinkedList<>();

  public void offer(T value) {
    synchronized (this) {
      queue.offer(value);
      notifyAll();
    }
  }

  public T poll() {
    synchronized (this) {
      while (queue.isEmpty()) {
        try {
          wait();
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
      return queue.poll();
    }
  }
}
