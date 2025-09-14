package com.example.mywallet.helper;

import org.slf4j.Logger;

public class LogHelper {

  private static final String PREFIX = "[{}] {}";

  private LogHelper() {
  }

  public static void warn(Logger log, String message, Object... args) {
    if (log.isWarnEnabled()) {
      log.warn(PREFIX, getSimpleClassName(log), String.format(message, args));
    }
  }

  public static void info(Logger log, String message, Object... args) {
    if (log.isInfoEnabled()) {
      log.info(PREFIX, getSimpleClassName(log), String.format(message, args));
    }
  }

  public static void error(Logger log, String message, Object... args) {
    if (log.isErrorEnabled()) {
      log.error(PREFIX, getSimpleClassName(log), String.format(message, args));
    }
  }

  private static String getSimpleClassName(Logger log) {
    String fullClassName = log.getName();
    return fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
  }
}
