package com.codeacademy.pastryshop.controller;

import com.codeacademy.pastryshop.model.*;
import com.codeacademy.pastryshop.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/payment")
@Validated
public class PaymentController
{
  private final ContractService contractService;

  private final PaymentService paymentService;

  private final BusinessService businessService;

  private final CompanyService companyService;

  private final BankAccountServiceImpl bankAccountService;

  public PaymentController(ContractService contractService, PaymentService paymentService, BusinessService businessService, CompanyService companyService, BankAccountServiceImpl bankAccountService)
  {
    this.contractService = contractService;
    this.paymentService = paymentService;
    this.businessService = businessService;
    this.companyService = companyService;
    this.bankAccountService = bankAccountService;
  }

  @PostMapping("/rent/{id}")
  public ResponseEntity<?> payRent(@PathVariable(value = "id") Long id,
                                   @RequestParam Long shopIban,
                                   @RequestParam Long mallIban,
                                   Principal principal)
  {

    Contract contract = contractService.getContract(id);

    BusinessEntity shop = contract.getShop();

    BankAccount shopAccount = bankAccountService.getBankAccountById(shopIban);
    BankAccount mallAccount = bankAccountService.getBankAccountById(mallIban);

    Company company = (Company) companyService.findCompanyByUsername(principal.getName());

    if (shop.getOwner().equals(company)) {
      if (paymentService.payRent(shop, contract, shopAccount, mallAccount)) {
        return new ResponseEntity<>("Rent payment successful.",HttpStatus.OK);
      }
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/supplier/{id}")
  public ResponseEntity<?> paySupplier(@PathVariable(value = "id") Long id,
                                       @RequestParam Long shopIban,
                                       @RequestParam Long supplierIban,
                                       Principal principal)
  {

    Contract contract = contractService.getContract(id);

    BusinessEntity shop = contract.getShop();

    BankAccount shopAccount = bankAccountService.getBankAccountById(shopIban);
    BankAccount supplierAccount = bankAccountService.getBankAccountById(supplierIban);

    Company company = (Company) companyService.findCompanyByUsername(principal.getName());

    if (shop.getOwner().equals(company)) {
      if (paymentService.paySupplier(shop, contract, shopAccount, supplierAccount)) {
        return new ResponseEntity<>("Supplier payment successful.",HttpStatus.OK);
      }
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/{id}")
  public ResponseEntity<?> payShop(@PathVariable(value = "id") Long id,
                                   @RequestParam Long shopIban,
                                   @RequestParam Long clientIban,
                                   @RequestParam BigDecimal bill,
                                   Principal principal) throws Exception
  {
    BusinessEntity shop = businessService.getBusinessByID(id);

    BankAccount shopAccount = bankAccountService.getBankAccountById(shopIban);
    BankAccount clientAccount = bankAccountService.getBankAccountById(clientIban);

    User client = companyService.findCompanyByUsername(principal.getName());

    if (clientAccount.getOwner().equals(client)) {
      if (paymentService.payShop(shop, clientAccount, shopAccount, bill)) {
        return new ResponseEntity<>("Payment successful.",HttpStatus.OK);
      }
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }
}
