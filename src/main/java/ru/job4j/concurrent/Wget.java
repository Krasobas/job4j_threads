package ru.job4j.concurrent;

public class Wget {
  public static void main(String[] args) {
    Thread loader = new Thread(() -> {
      try {
        for (int index = 0; index < 101; index++) {
          System.out.print("\rLoading : " + index  + "%");
          Thread.sleep(1000);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
    loader.start();
  }
}
