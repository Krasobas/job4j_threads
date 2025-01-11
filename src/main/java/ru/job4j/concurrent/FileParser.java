package ru.job4j.concurrent;

import java.io.*;
import java.util.function.Predicate;

public class FileParser {
  private final File file;

  public FileParser(File file) {
    this.file = file;
  }

  public synchronized String getContent(Predicate<Character> filter) throws IOException {

    StringBuilder builder = new StringBuilder();

    try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(file))) {
      char data;
      while ((data = (char) input.read()) > 0) {
        if (filter.test(data)) {
          builder.append(data);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return builder.toString();
  }

  public synchronized String getContentWithoutUnicode() throws IOException {
    return getContent(data -> data < 0x80);
  }
}
