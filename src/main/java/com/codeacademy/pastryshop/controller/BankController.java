package com.codeacademy.pastryshop.controller;

import com.codeacademy.pastryshop.dto.BankAccountDto;
import com.codeacademy.pastryshop.dto.GetBankAccountInfoDto;
import com.codeacademy.pastryshop.model.BankAccount;
import com.codeacademy.pastryshop.model.Company;
import com.codeacademy.pastryshop.service.BankAccountService;
import com.codeacademy.pastryshop.service.CompanyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/bank")
@Validated
public class BankController
{

  private final BankAccountService bankAccountService;

  private final CompanyServiceImpl companyService;

  @Autowired
  public BankController(BankAccountService bankAccountService, CompanyServiceImpl companyService)
  {
    this.bankAccountService = bankAccountService;
    this.companyService = companyService;
  }

  @PostMapping("/open")
  @ResponseBody
  public ResponseEntity<HttpStatus> openNewBankAccount(@Valid @RequestBody BankAccountDto bankAccountDto, Principal principal)
  {
    Company currentCompanyOrUser = (Company) companyService.findCompanyByUsername(principal.getName());

    bankAccountService.openNewBankAccount(bankAccountDto.getBalance(), bankAccountDto.getCurrency(), currentCompanyOrUser);

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping("/close/{id}")
  @ResponseBody
  public ResponseEntity<HttpStatus> closeBankAccount(@PathVariable(value = "id") Long id,
                                            Principal principal)
  {
    Company currentCompanyOrUser = (Company) companyService.findCompanyByUsername(principal.getName());

    BankAccount targetAccount = bankAccountService.getBankAccountById(id);

    if (bankAccountService.cancelBankAccount(currentCompanyOrUser, targetAccount)) {
      return new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/deposit/{id}")
  @ResponseBody
  public ResponseEntity<HttpStatus> depositMoney(@PathVariable(value = "id") Long id,
                                        @RequestParam BigDecimal amount)
  {
    BankAccount targetAccount = bankAccountService.getBankAccountById(id);
    if (bankAccountService.deposit(targetAccount, amount)) {
      return new ResponseEntity<>(HttpStatus.OK);
    }

    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/withdraw/{id}")
  @ResponseBody
  public ResponseEntity<HttpStatus> withdrawMoney(@PathVariable(value = "id") Long id,
                                         @RequestParam BigDecimal amount)
  {
    BankAccount targetAccount = bankAccountService.getBankAccountById(id);
    if (bankAccountService.withdraw(targetAccount, amount)) {
      return new ResponseEntity<>(HttpStatus.OK);
    }

    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/change/{id}")
  @ResponseBody
  public ResponseEntity<HttpStatus> changeCurrency(@PathVariable(value = "id") Long id,
                                          @RequestParam BankAccount.Currency currency,
                                          Principal principal)
  {
    BankAccount targetAccount = bankAccountService.getBankAccountById(id);

    Company currentCompanyOrUser = (Company) companyService.findCompanyByUsername(principal.getName());

    if (
        bankAccountService.changeCurrency(targetAccount, currency)) {
        return new ResponseEntity<>(HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @GetMapping("/check/{id}")
  @ResponseBody
  public ResponseEntity<GetBankAccountInfoDto> checkAccountInfo(@PathVariable(value = "id") Long id,
                                                                Principal principal)
  {
    BankAccount targetAccount = bankAccountService.getBankAccountById(id);
    Company currentCompanyOrUser = (Company) companyService.findCompanyByUsername(principal.getName());

    if (targetAccount.getOwner().equals(currentCompanyOrUser)) {
      GetBankAccountInfoDto bankAccountInfoDto = new GetBankAccountInfoDto(targetAccount.getId(), targetAccount.getCurrency(), targetAccount.getOwner().getUsername(), targetAccount.getBalance());
      return new ResponseEntity<>(bankAccountInfoDto, HttpStatus.OK);
    }

    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/transfer")
  @ResponseBody
  public ResponseEntity<HttpStatus> transferMoney(@RequestParam Long userAccountId,
                                         @RequestParam Long targetId,
                                         @RequestParam BigDecimal amount,
                                         Principal principal)
  {
    BankAccount userAccount = bankAccountService.getBankAccountById(userAccountId);
    BankAccount targetAccount = bankAccountService.getBankAccountById(targetId);

    Company currentCompanyOrUser = (Company) companyService.findCompanyByUsername(principal.getName());

    if (userAccount.getOwner().equals(currentCompanyOrUser) &&
        bankAccountService.transferMoney(userAccount, amount, targetAccount)
    ) {
        return new ResponseEntity<>(HttpStatus.OK);

    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }
}
