package com.codeacademy.pastryshop.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDate;

@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler
{
  @ExceptionHandler(UserAlreadyExistException.class)
  public final ResponseEntity<Object> handleUserAlreadyExistExceptionExceptions(UserAlreadyExistException ex)
  {
    CustomExceptionSchema exceptionResponse =
        new CustomExceptionSchema(
            ex.getMessage(), LocalDate.now());
    return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(BadCredentialsException.class)
  public final ResponseEntity<Object> handleBadCredentialsExceptionExceptions(BadCredentialsException ex)
  {
    DefaultExceptionSchema exceptionResponse =
        new DefaultExceptionSchema(
            ex.getLocalizedMessage());
    return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(javax.validation.ConstraintViolationException.class)
  public final ResponseEntity<Object> handleConstraintViolationExceptionExceptions(javax.validation.ConstraintViolationException ex)
  {
    CustomExceptionSchema exceptionResponse =
        new CustomExceptionSchema(
            ex.getMessage(), LocalDate.now());
    return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
  {
    DefaultExceptionSchema exceptionResponse =
        new DefaultExceptionSchema(
            ex.getMessage());
    return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
      HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request)
  {
    DefaultExceptionSchema exceptionResponse =
        new DefaultExceptionSchema(
            ex.getLocalizedMessage());
    return new ResponseEntity<>(exceptionResponse, HttpStatus.METHOD_NOT_ALLOWED);
  }

  @ExceptionHandler(NotCompanyException.class)
  public final ResponseEntity<Object> handleNotCompanyExceptions(NotCompanyException ex)
  {
    CustomExceptionSchema exceptionResponse =
        new CustomExceptionSchema(
            ex.getMessage(), LocalDate.now());
    return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
  }

}
