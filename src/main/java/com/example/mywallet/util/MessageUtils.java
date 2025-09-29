package com.example.mywallet.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class MessageUtils {

  private MessageUtils() {
  }

  private static MessageSource messageSource;

  public static void setMessageSource(MessageSource source) {
    messageSource = source;
  }

  public static String getMessage(String code, Object[] args) {
    return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
  }

  public static String getMessage(String code) {
    return getMessage(code, null);
  }
}