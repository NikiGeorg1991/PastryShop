package com.codeacademy.pastryshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class LogInUserDto
{
  @NotBlank(message = "You must enter your username!")
  private String username;

  @NotBlank(message = "You must enter your password!")
  private String password;
}
