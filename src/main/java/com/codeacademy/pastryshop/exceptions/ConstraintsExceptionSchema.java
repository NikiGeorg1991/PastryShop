package com.codeacademy.pastryshop.exceptions;

import lombok.Getter;
import lombok.Setter;

import javax.validation.ConstraintViolation;
import java.util.Set;

@Getter
@Setter
public class ConstraintsExceptionSchema
{
  private String    message;
  private Set<ConstraintViolation<?>> violatedConstraints;

  protected ConstraintsExceptionSchema() {}

  public ConstraintsExceptionSchema(
      String message, Set<ConstraintViolation<?>> violatedConstraints) {
    this.message = message;
    this.violatedConstraints = violatedConstraints;
  }
}
