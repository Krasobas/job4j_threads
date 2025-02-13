package ru.job4j.threads;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class RaceConditionExample {
  private static final int THREAD_COUNT = 3;
  private static CyclicBarrier barrier;

  public static void main(String[] args) {
    barrier = new CyclicBarrier(THREAD_COUNT, () -> System.out.println("All threads have finished one round."));

    for (int i = 0; i < THREAD_COUNT; i++) {
      Thread thread = new Thread(new WorkerThread());
      thread.start();
    }
  }

  static class WorkerThread implements Runnable {
    @Override
    public void run() {
      for (int round = 0; round < 3; round++) {
        try {
          System.out.println(Thread.currentThread().getName() + " doing work for round " + (round + 1));
          Thread.sleep(1000);
          barrier.await();
          System.out.println(Thread.currentThread().getName() + " finished round " + (round + 1));
        } catch (InterruptedException | BrokenBarrierException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
