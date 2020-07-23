package com.codeacademy.pastryshop.controller;

import com.codeacademy.pastryshop.dto.CompanyWithoutPassword;
import com.codeacademy.pastryshop.model.Company;
import com.codeacademy.pastryshop.model.User;
import com.codeacademy.pastryshop.service.BusinessServiceImpl;
import com.codeacademy.pastryshop.service.CompanyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users")
@Validated
public class UserController
{
  private final CompanyServiceImpl companyService;

  private final BusinessServiceImpl businessService;

  @Autowired
  public UserController(CompanyServiceImpl companyService, BusinessServiceImpl businessService)
  {
    this.companyService = companyService;
    this.businessService = businessService;
  }

  @GetMapping("/details")
  @ResponseBody
  public ResponseEntity<CompanyWithoutPassword> showCurrentUser(Principal principal)
  {
    Company currentCompanyOrUser = (Company) companyService.findCompanyByUsername(principal.getName());
    CompanyWithoutPassword companyOrUserDto = new CompanyWithoutPassword(currentCompanyOrUser.getUsername(), currentCompanyOrUser.getFirstName(),
        currentCompanyOrUser.getLastName(), currentCompanyOrUser.getEmail(), currentCompanyOrUser.getPhoneNumber(), currentCompanyOrUser.getBulstat());
    return ResponseEntity.ok(companyOrUserDto);
  }


  @PostMapping("/bulstat")
  @ResponseBody
  public ResponseEntity<HttpStatus> becomeCompany(Principal principal, @RequestParam(value = "bulstat") @Valid final String bulstat)
  {
    User user = companyService.findCompanyByUsername(principal.getName());

    if (!businessService.checkIfIsCompany((Company) user))
    {
      businessService.changeBulstat((Company) user, bulstat);
      return new ResponseEntity<>(HttpStatus.OK);
    }
    else
    {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @PutMapping("/password")
  @ResponseBody
  public ResponseEntity<HttpStatus> changePassword(@RequestParam(value = "password") String password,
                                                @RequestParam(value = "passwordConfirmation") String passwordConfirmation,
                                                Principal principal) {

    User user = companyService.findCompanyByUsername(principal.getName());
    if ((!(user.getPassword().equals(password)) && password.equals(passwordConfirmation))) {
      companyService.changePassword(user, password);
      return new ResponseEntity<>(HttpStatus.ACCEPTED);
    } else {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

}

