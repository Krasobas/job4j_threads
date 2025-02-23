package ru.job4j;

import java.util.concurrent.*;

public class RunnableAndCollable {
  public static void main(String[] args) throws ExecutionException, InterruptedException {
    new Thread(() -> System.out.println("hello")).start();
    Executors.newCachedThreadPool().submit(() -> "hello").get();
    Callable<String> task = () -> "FutureTask!";
    FutureTask<String> future = new FutureTask<>(task);
    new Thread(future).start();
    System.out.println(future.get());
  }
}
