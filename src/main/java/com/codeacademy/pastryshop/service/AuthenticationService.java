package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.dto.LogInUserDto;

public interface AuthenticationService
{
  boolean authenticateUser(LogInUserDto logInUserDto);
}
