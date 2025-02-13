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
    return Objects.nonNull(account) && Objects.isNull(accounts.putIfAbsent(account.id(), account));
  }

  public synchronized boolean update(Account account) {
    return Objects.nonNull(account) && Objects.nonNull(accounts.replace(account.id(), account));
  }

  public synchronized void delete(int id) {
    accounts.remove(id);
  }

  public synchronized Optional<Account> getById(int id) {
    return  Optional.ofNullable(accounts.get(id));
  }

  public synchronized boolean transfer(int fromId, int toId, int amount) {
    Optional<Account> fromOpt = getById(fromId);
    Optional<Account> toOpt = getById(toId);
    if (fromOpt.isEmpty() || toOpt.isEmpty()) {
      return false;
    }
    Account from = fromOpt.get();
    if (from.amount() < amount) {
      return false;
    }
    accounts.put(fromId, new Account(fromId, from.amount() - amount));
    Account to = toOpt.get();
    accounts.put(toId, new Account(toId, to.amount() + amount));

    return true;
  }
}
