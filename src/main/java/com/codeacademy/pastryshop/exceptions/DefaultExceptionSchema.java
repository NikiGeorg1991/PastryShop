package com.codeacademy.pastryshop.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DefaultExceptionSchema
{
  private String details;

  protected DefaultExceptionSchema() {}

  public DefaultExceptionSchema(
      String details) {
    this.details = details;
  }
}
