package ru.job4j.pool;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class ParallelIndexSearch<T> extends RecursiveTask<Integer> {
  private final T[] array;
  private final T target;

  private final int start;
  private final int end;

  public ParallelIndexSearch(T[] array, T target, int start, int end) {
    this.array = array;
    this.target = target;
    this.start = start;
    this.end = end;
  }

  @Override
  protected Integer compute() {
    int index = -1;
    if ((end - start) <= 10) {
      for (int i = start; i < end; i++) {
        if (target.equals(array[i])) {
          return i;
        }
      }
    } else {
      int middle = start + (end - start) / 2;
      ParallelIndexSearch<T> left = new ParallelIndexSearch<>(array, target, start, middle);
      ParallelIndexSearch<T> right = new ParallelIndexSearch<>(array, target, middle, end);
      left.fork();

      int leftResult = left.join();
      int rightResult = right.compute();

      return leftResult != -1 ? leftResult : rightResult;

    }
    return index;
  }

  public static <T> int indexOf(T[] array, T value) {
    try (ForkJoinPool pool = new ForkJoinPool()) {
      return pool.invoke(new ParallelIndexSearch<T>(array, value, 0, array.length));
    }

  }

  public static void main(String[] args) {
    Integer[] array = IntStream.range(0, 100)
        .boxed()
        .map(i -> {
          if (i == 98) {
            return 123;
          }
          return ThreadLocalRandom.current().nextInt(0, 100 + 1);
        })
        .toArray(Integer[]::new);
    System.out.println(indexOf(array, 123));
  }
}
