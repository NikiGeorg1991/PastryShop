package com.codeacademy.pastryshop.exceptions;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class CustomExceptionSchema
{
  private   String    message;
  private LocalDate timestamp;

  protected CustomExceptionSchema() {}

  public CustomExceptionSchema(
      String message, LocalDate timestamp) {
    this.message = message;
    this.timestamp = timestamp;
  }
}
