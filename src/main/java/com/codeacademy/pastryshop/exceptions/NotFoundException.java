package com.codeacademy.pastryshop.exceptions;

public class NotFoundException extends RuntimeException
{
  public NotFoundException(String s){
    super(s);
  }
}
