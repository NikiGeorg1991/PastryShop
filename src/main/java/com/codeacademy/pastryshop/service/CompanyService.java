package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.dto.RegistrationDto;
import com.codeacademy.pastryshop.exceptions.UserAlreadyExistException;
import com.codeacademy.pastryshop.model.Company;
import com.codeacademy.pastryshop.model.User;

import java.util.Optional;

public interface CompanyService
{
  void registerCompany(RegistrationDto registerCompanyDto) throws UserAlreadyExistException;

  User findCompanyByUsername(String username);

  Optional<User> getCompanyByID(long id);

  boolean usernameExists(String username);

  void deleteCompany(Company company);
}
