package com.example.mywallet.util;

import jakarta.annotation.PostConstruct;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class MessageUtilsInjector {

  private final MessageSource messageSource;

  public MessageUtilsInjector(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @PostConstruct
  public void init() {
    MessageUtils.setMessageSource(messageSource);
  }
}