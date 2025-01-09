package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {
  @Override
  public void run() {
    int count = 0;
    var process = new char[] {'-', '\\', '|', '/'};
    while (!Thread.currentThread().isInterrupted()) {
      if (count == process.length) {
        count = 0;
      }
      System.out.print("\r Loading ... " + process[count++]);
    }
  }

  public static void main(String[] args) {
    Thread progress = new Thread(new ConsoleProgress());
    progress.start();
    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    progress.interrupt();
  }
}
