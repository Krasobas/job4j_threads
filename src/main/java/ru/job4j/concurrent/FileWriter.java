package ru.job4j.concurrent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileWriter {
  private final File file;

  public FileWriter(File file) {
    this.file = file;
  }

  public synchronized void saveContent(String content) throws IOException {
    Files.writeString(file.toPath(), content);
  }
}
