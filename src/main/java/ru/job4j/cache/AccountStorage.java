package ru.job4j.cache;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
@ThreadSafe
public class AccountStorage {
  @GuardedBy("this")
  private final HashMap<Integer, Account> accounts = new HashMap<>();

  public synchronized boolean add(Account account) {
    if (Objects.isNull(account)) {
      return false;
    }
    accounts.put(account.id(), account);
    return true;
  }

  public synchronized boolean update(Account account) {
    if (Objects.isNull(account) || !accounts.containsKey(account.id())) {
      return false;
    }
    accounts.put(account.id(), account);
    return true;
  }

  public synchronized void delete(int id) {
    accounts.remove(id);
  }

  public synchronized Optional<Account> getById(int id) {
    return  Optional.ofNullable(accounts.get(id));
  }

  public synchronized boolean transfer(int fromId, int toId, int amount) {
    if (!accounts.containsKey(fromId) || !accounts.containsKey(toId)) {
      return false;
    }
    Account from = accounts.get(fromId);
    if (from.amount() < amount) {
      return false;
    }
    accounts.put(fromId, new Account(fromId, from.amount() - amount));
    Account to = accounts.get(toId);
    accounts.put(toId, new Account(toId, to.amount() + amount));

    return true;
  }
}
