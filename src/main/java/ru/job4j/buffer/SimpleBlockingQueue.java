package ru.job4j.buffer;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.LinkedList;
import java.util.Queue;

@ThreadSafe
public class SimpleBlockingQueue<T> {

  @GuardedBy("this")
  private final Queue<T> queue;
  private final int size;

  public SimpleBlockingQueue(int size) {
    this.size = size;
    this.queue = new LinkedList<>();
  }

  public void offer(T value) throws InterruptedException {
    synchronized (this) {
      while (queue.size() >= size) {
        wait();
      }
      queue.offer(value);
      notifyAll();
    }
  }

  public T poll() throws InterruptedException {
    synchronized (this) {
      while (queue.isEmpty()) {
        wait();
      }
      T next = queue.poll();
      notifyAll();
      return next;
    }
  }
}
