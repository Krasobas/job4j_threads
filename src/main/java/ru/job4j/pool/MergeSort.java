package ru.job4j.pool;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

public class MergeSort {

  public static int[] sort(int[] array) {
    return sort(array, 0, array.length - 1);
  }

  private static int[] sort(int[] array, int from, int to) {
    if (from == to) {
      return new int[] {array[from]};
    }
    int middle = (from + to) / 2;
    return merge(
        sort(array, from, middle),
        sort(array, middle + 1, to)
    );
  }

  public static int[] merge(int[] left, int[] right) {
    int leftI = 0;
    int rightI = 0;
    int resultI = 0;
    int[] result = new int[left.length + right.length];
    while (resultI != result.length) {
      if (leftI == left.length) {
        result[resultI++] = right[rightI++];
      } else if (rightI == right.length) {
        result[resultI++] = left[leftI++];
      } else if (left[leftI] <= right[rightI]) {
        result[resultI++] = left[leftI++];
      } else {
        result[resultI++] = right[rightI++];
      }
    }
    return result;
  }

  public static void main(String[] args) {
    int[] arr = IntStream.range(0, 100).unordered().map(i -> ThreadLocalRandom.current().nextInt(0, 100 + 1)).toArray();
    System.out.println(Arrays.toString(arr));
    System.out.println(Arrays.toString(sort(arr)));
  }
}
