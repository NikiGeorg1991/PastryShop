package com.codeacademy.pastryshop.exceptions;

public class FailedPaymentException extends RuntimeException
{
  public FailedPaymentException(String s){
    super(s);
  }
}
