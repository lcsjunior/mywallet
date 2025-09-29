package com.example.mywallet.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.context.annotation.Configuration;

import static java.nio.charset.StandardCharsets.UTF_8;

@Configuration
public class MessageSourceConfig {
  private static final String BASENAME = "messages";

  @Bean
  public MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename(BASENAME);
    messageSource.setDefaultEncoding(UTF_8.name());
    return messageSource;
  }
}