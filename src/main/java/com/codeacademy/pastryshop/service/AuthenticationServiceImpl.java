package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.dto.LogInUserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService
{
  private final AuthenticationManager authenticationManager;

  @Autowired
  public AuthenticationServiceImpl(AuthenticationManager authenticationManager)
  {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public boolean authenticateUser(LogInUserDto logInUserDto)
  {
    return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInUserDto.getUsername(),
        logInUserDto.getPassword())) != null;
  }
}
