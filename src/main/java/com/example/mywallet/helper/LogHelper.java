package com.example.mywallet.helper;

import org.slf4j.Logger;

public class LogHelper {

  private LogHelper() {
  }

  public static void warn(Logger log, String message, Object... args) {
    log.warn("[{}] {}", getSimpleClassName(log), String.format(message, args));
  }

  public static void info(Logger log, String message, Object... args) {
    log.info("[{}] {}", getSimpleClassName(log), String.format(message, args));
  }

  public static void error(Logger log, String message, Object... args) {
    log.error("[{}] {}", getSimpleClassName(log), String.format(message, args));
  }

  private static String getSimpleClassName(Logger log) {
    String fullClassName = log.getName();
    return fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
  }
}
