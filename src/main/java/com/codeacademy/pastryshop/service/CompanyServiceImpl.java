package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.dto.RegistrationDto;
import com.codeacademy.pastryshop.exceptions.UserAlreadyExistException;
import com.codeacademy.pastryshop.model.Company;
import com.codeacademy.pastryshop.model.User;
import com.codeacademy.pastryshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@Service
@Validated
public class CompanyServiceImpl implements CompanyService
{
  private final UserRepository userRepository;

  private final PasswordEncoder passwordEncoder;

  private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

  @Autowired
  public CompanyServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder)
  {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }


  @Override
  public void registerCompany(RegistrationDto registerCompanyDto) throws UserAlreadyExistException
  {
    if (usernameExists(registerCompanyDto.getUsername())) {
      throw new UserAlreadyExistException("User with that username already exist, please pick another one!");
    }
    final Company company = new Company(registerCompanyDto.getUsername(),
        passwordEncoder.encode(registerCompanyDto.getPassword()),
        registerCompanyDto.getFirstName(),
        registerCompanyDto.getLastName(),
        registerCompanyDto.getEmail(),
        registerCompanyDto.getPhoneNumber(),
        registerCompanyDto.getBulstat());

    userRepository.save(company);
  }

  @Override
  public void deleteCompany(Company company)
  {
    userRepository.delete(company);
  }

  @Override
  public User findCompanyByUsername(String username)
  {
    return userRepository.findByUsername(username);
//    return Optional.ofNullable(userRepository.findByUsername(username).orElse(null))
  }

  @Override
  public Optional<User> getCompanyByID(long id)
  {
    return userRepository.findById(id);
  }

  @Override
  public boolean usernameExists(String username)
  {
    return userRepository.findByUsername(username) != null;
  }

  public Boolean changePassword(User user, String password) {
    user.setPassword(bCryptPasswordEncoder.encode(password));
    userRepository.save(user);
    return true;
  }
}
