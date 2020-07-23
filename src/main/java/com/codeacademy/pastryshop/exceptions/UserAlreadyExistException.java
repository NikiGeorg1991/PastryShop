package com.codeacademy.pastryshop.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserAlreadyExistException extends RuntimeException
{
    private final String message;

    public UserAlreadyExistException(String message) {
      this.message = message;
    }

}
