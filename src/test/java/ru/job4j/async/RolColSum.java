package ru.job4j.async;

import lombok.*;

import java.util.concurrent.CompletableFuture;


public class RolColSum {
  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  @EqualsAndHashCode
  public static class Sums {
    private int rowSum;
    private int colSum;
  }

  public static Sums[] sum(int[][] matrix) {
    int size = matrix.length;
    Sums[] sums = new Sums[size];

    for (int i = 0; i < size; i++) {
      int rowSum = 0;
      int colSum = 0;
      for (int j = 0; j < size; j++) {
        rowSum += matrix[i][j];
        colSum += matrix[i][i];
      }
      sums[i] = new Sums(rowSum, colSum);
    }
    return sums;
  }

  public static Sums[] asyncSum(int[][] matrix) {
    int size = matrix.length;
    Sums[] sums = new Sums[size];
    CompletableFuture<Void>[] futures = new CompletableFuture[size];
    for (int i = 0; i < size; i++) {
      futures[i] = getTask(matrix, i, sums);
    }
    CompletableFuture.allOf(futures);
    return sums;
  }

  public static CompletableFuture<Void> getTask(int[][] matrix, int i, Sums[] sums) {
    return CompletableFuture.runAsync(() -> {
      int rowSum = 0;
      int colSum = 0;
      for (int j = 0; j < matrix.length; j++) {
        rowSum += matrix[i][j];
        colSum += matrix[i][i];
      }
      sums[i] = new Sums(rowSum, colSum);
    });
  }

  public static void main(String[] args) {
    int[][] matrix = new int[][] {{1, 2, 3}, {1, 2, 3}, {1, 2, 3}};
    Sums[] result = asyncSum(matrix);
    System.out.println(result[2].getRowSum());
    System.out.println(result[2].getColSum());
  }
}
