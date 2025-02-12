package ru.job4j.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Objects;

public class Wget implements Runnable {
  private final String url;
  private final int speed;
  private static final int BUFFER_SIZE = 1024;

  public Wget(String url, int speed) {
    this.url = url;
    this.speed = speed;
  }

  @Override
  public void run() {
    long startAt = System.currentTimeMillis();
    String fileName = url.substring(url.lastIndexOf("/") + 1);
    File file = new File(fileName);
    try (InputStream input = new URL(url).openStream(); FileOutputStream output = new FileOutputStream(file)) {
      System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
      byte[] dataBuffer = new byte[BUFFER_SIZE];
      int bytesRead;
      int bytesCount = 0;
      long time = System.currentTimeMillis();
      while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
        output.write(dataBuffer, 0, bytesRead);
        bytesCount += bytesRead;
        long interval = System.currentTimeMillis() - time;
        if (interval < 1000) {
          sleep(interval);
        }
        bytesCount = 0;
        time = System.currentTimeMillis();
      }
      System.out.println(Files.size(file.toPath()) + " bytes");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    int argsLength = args.length;
    if (argsLength != 2) {

      throw new IllegalArgumentException(String.format("Required exactly 2 arguments, but was provided %d. Usage: [URL] [SPEED]%n", argsLength));
    }
    String url = args[0];
    if (!isValidURL(url)) {
      throw new IllegalArgumentException(String.format("Invalid url: %s%n", url));
    }

    int speed = Integer.parseInt(args[1]);

    Thread wget = new Thread(new Wget(url, speed));
    wget.start();
    wget.join();
  }

  private static boolean isValidURL(String url) {
    if (Objects.isNull(url) || url.isBlank()) {
      return false;
    }
    try {
      new URL(url).toURI();
      return true;
    } catch (MalformedURLException | URISyntaxException e) {
      return false;
    }
  }

  private static void sleep(long interval) {
    try {
      long toSleep = 1000 - interval;
      System.out.printf("Sleeping %d ms%n", toSleep);
      Thread.sleep(toSleep);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
