package ru.job4j.pool;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.job4j.pool.ParallelIndexSearch.indexOf;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

class ParallelIndexSearchTest {

  @Test
  void whenIntArrayThenOk() {
    int target = 123;
    int expected = 98;
    Integer[] array = IntStream.range(0, 100)
        .boxed()
        .map(i -> {
          if (i == expected) {
            return target;
          }
          return ThreadLocalRandom.current().nextInt(0, 100 + 1);
        })
        .toArray(Integer[]::new);
    int got = indexOf(array, target);
    assertThat(got).isEqualTo(expected);
  }

  @Test
  void whenStringArrayThenOk() {
    String target = "Hello";
    int expected = 98;
    String[] array = IntStream.range(0, 100)
        .boxed()
        .map(i -> {
          if (i == expected) {
            return target;
          }
          return "Bonjour";
        })
        .toArray(String[]::new);
    int got = indexOf(array, target);
    assertThat(got).isEqualTo(expected);
  }

  @Test
  void whenSmallArrayThenLinerSearch() {
    String target = "Hello";
    int expected = 8;
    String[] array = IntStream.range(0, 9)
        .boxed()
        .map(i -> {
          if (i == expected) {
            return target;
          }
          return "Bonjour";
        })
        .toArray(String[]::new);
    int got = indexOf(array, target);
    assertThat(got).isEqualTo(expected);
  }

  @Test
  void whenBigArrayThenParallelSearch() {
    String target = "Hello";
    int expected = 988;
    String[] array = IntStream.range(0, 1000)
        .boxed()
        .map(i -> {
          if (i == expected) {
            return target;
          }
          return "Bonjour";
        })
        .toArray(String[]::new);
    int got = indexOf(array, target);
    assertThat(got).isEqualTo(expected);
  }

  @Test
  void whenNotFoundThenReturnMinusOne() {
    String target = "Hello";
    int expected = -1;
    String[] array = IntStream.range(0, 1000)
        .boxed()
        .map(i -> "Bonjour")
        .toArray(String[]::new);
    int got = indexOf(array, target);
    assertThat(got).isEqualTo(expected);
  }

}