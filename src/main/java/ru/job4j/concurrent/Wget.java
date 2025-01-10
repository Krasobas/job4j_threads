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
    File file = new File("tmp.xml");
    try (InputStream input = new URL(url).openStream();
         FileOutputStream output = new FileOutputStream(file)) {
      System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
      byte[] dataBuffer = new byte[BUFFER_SIZE];
      int bytesRead;
      while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
        long downloadAt = System.nanoTime();
        output.write(dataBuffer, 0, bytesRead);
        long time = System.nanoTime() - downloadAt;
        long bytePerMs =  Math.round(BUFFER_SIZE / (double) time * 1000000);
        long toSleep = bytePerMs / speed;
        if (toSleep <= 0) {
          continue;
        }
        try {
          System.out.printf("Sleeping %d ms%n", toSleep);
          Thread.sleep(toSleep);
        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
        }
      }
      System.out.println(Files.size(file.toPath()) + " bytes");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws InterruptedException {
    int argsLength = args.length;
    if (argsLength != 2) {
      System.out.printf("Required exactly 2 arguments, but was provided %d. Usage: [URL] [SPEED]%n", argsLength);
      return;
    }
    String url = args[0];
    if (!isValidURL(url)) {
      System.out.printf("Invalid url: %s%n", url);
      return;
    }

    String speedStr = args[1];
    int speed;
    try {
      speed = Integer.parseInt(args[1]);
    } catch (NumberFormatException e) {
      System.out.printf("Speed should be a number, but was provided: %s%n", speedStr);
      return;
    }
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
}
