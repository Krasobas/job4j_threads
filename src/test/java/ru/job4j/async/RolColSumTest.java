package ru.job4j.async;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.job4j.async.RolColSum.asyncSum;
import static ru.job4j.async.RolColSum.sum;

import org.junit.jupiter.api.Test;
class RolColSumTest {

  @Test
  void whenSyncSumWhenOk() {
    int[][] matrix = new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    RolColSum.Sums first = new RolColSum.Sums(6, 12);
    RolColSum.Sums second = new RolColSum.Sums(15, 15);
    RolColSum.Sums third = new RolColSum.Sums(24, 18);
    RolColSum.Sums[] expected = new RolColSum.Sums[]{first, second, third};
    RolColSum.Sums[] result = sum(matrix);
    assertThat(result).isEqualTo(expected);
  }

  @Test
  void whenAsyncSumWhenOk() {
    int[][] matrix = new int[][] {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
    RolColSum.Sums first = new RolColSum.Sums(6, 12);
    RolColSum.Sums second = new RolColSum.Sums(15, 15);
    RolColSum.Sums third = new RolColSum.Sums(24, 18);
    RolColSum.Sums[] expected = new RolColSum.Sums[]{first, second, third};
    RolColSum.Sums[] result = asyncSum(matrix);
    assertThat(result).isEqualTo(expected);
  }

}