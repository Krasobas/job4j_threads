package ru.job4j.cache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Cache {
  private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

  public boolean add(Base model) {
    return Objects.nonNull(model) && Objects.nonNull(memory.putIfAbsent(model.id(), model));
  }

  public boolean update(Base model) throws OptimisticException {
    if (Objects.isNull(model)) {
      return false;
    }
    Base result = memory.computeIfPresent(model.id(), (key, value) -> {
        if (value.version() != model.version()) {
          throw new OptimisticException("Versions are not equal");
        }

        return new Base(model.id(), model.name(), model.version() + 1);
    });
    return Objects.nonNull(result);
  }

  public void delete(int id) {
    memory.remove(id);
  }

  public Optional<Base> findById(int id) {
    return Stream.of(memory.get(id))
        .filter(Objects::nonNull)
        .findFirst();
  }
}
