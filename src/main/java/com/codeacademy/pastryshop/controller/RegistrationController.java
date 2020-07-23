package com.codeacademy.pastryshop.controller;

import com.codeacademy.pastryshop.dto.LogInUserDto;
import com.codeacademy.pastryshop.dto.RegistrationDto;
import com.codeacademy.pastryshop.service.CompanyServiceImpl;
import com.codeacademy.pastryshop.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Validated
public class RegistrationController
{
  private final JwtUtil jwtUtil;

  private final CompanyServiceImpl companyService;

  private final AuthenticationManager authenticationManager;


  @Autowired
  public RegistrationController(JwtUtil jwtUtil, AuthenticationManager authenticationManager,
                                CompanyServiceImpl companyService)
  {
    this.jwtUtil = jwtUtil;
    this.companyService = companyService;
    this.authenticationManager = authenticationManager;
  }

  @PostMapping("/register")
  @ResponseBody
  public ResponseEntity<HttpStatus> registerUserAccount(@Valid @RequestBody RegistrationDto registrationDto)
  {
    companyService.registerCompany(registrationDto);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }


  @PostMapping("/login")
  @ResponseBody
  public ResponseEntity<String> generateToken(@RequestParam(value = "username") @Valid final String username,
                              @RequestParam(value = "password") @Valid final String password) throws BadCredentialsException
  {
    LogInUserDto logInUserDto = new LogInUserDto(username, password);

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(logInUserDto.getUsername(), logInUserDto.getPassword())
      );
    }
    catch (BadCredentialsException ex) {
      throw new BadCredentialsException("Invalid username or password, please check your credentials again.");
    }
    return new ResponseEntity<>(jwtUtil.generateToken(logInUserDto.getUsername()), HttpStatus.OK);
  }

}
