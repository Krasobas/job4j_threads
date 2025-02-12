package ru.job4j.cache;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class AccountStorageTest {

  @Test
  void whenAdd() {
    var storage = new AccountStorage();
    storage.add(new Account(1, 100));
    var firstAccount = storage.getById(1)
        .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
    assertThat(firstAccount.amount()).isEqualTo(100);
  }

  @Test
  void whenUpdate() {
    var storage = new AccountStorage();
    storage.add(new Account(1, 100));
    storage.update(new Account(1, 200));
    var firstAccount = storage.getById(1)
        .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
    assertThat(firstAccount.amount()).isEqualTo(200);
  }

  @Test
  void whenDelete() {
    var storage = new AccountStorage();
    storage.add(new Account(1, 100));
    storage.delete(1);
    assertThat(storage.getById(1)).isEmpty();
  }

  @Test
  void whenTransfer() {
    var storage = new AccountStorage();
    storage.add(new Account(1, 100));
    storage.add(new Account(2, 100));
    boolean status = storage.transfer(1, 2, 100);
    var firstAccount = storage.getById(1)
        .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
    var secondAccount = storage.getById(2)
        .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
    assertThat(status).isTrue();
    assertThat(firstAccount.amount()).isZero();
    assertThat(secondAccount.amount()).isEqualTo(200);
  }

  @Test
  void whenNotEnoughMoney() {
    var storage = new AccountStorage();
    storage.add(new Account(1, 90));
    storage.add(new Account(2, 100));
    boolean status = storage.transfer(1, 2, 100);
    var firstAccount = storage.getById(1)
        .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
    var secondAccount = storage.getById(2)
        .orElseThrow(() -> new IllegalStateException("Not found account by id = 1"));
    assertThat(status).isFalse();
    assertThat(firstAccount.amount()).isEqualTo(90);
    assertThat(secondAccount.amount()).isEqualTo(100);
  }
}