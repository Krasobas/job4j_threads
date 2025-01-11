package ru.job4j.concurrent;

public class ShareNotSafe {
  public static void main(String[] args) throws InterruptedException {
    UserCache cache = new UserCache();
    User user = User.of("main");
    User user2 = User.of("main2");

    cache.add(user);
    cache.add(user2);

    Thread first = new Thread(() -> {
      for (int i = 0; i < 100; i++) {
        user.setName("first");
        user2.setName("first2");
      }
    });

    Thread second = new Thread(() -> {
      for (int i = 0; i < 100; i++) {
        user.setName("second");
        user2.setName("second2");
      }
    });

    first.start();
    second.start();
    first.join();
    second.join();

    System.out.println(cache.findById(1).getName());
    cache.findAll()
        .forEach(u -> System.out.println(u.getName()));
  }
}
