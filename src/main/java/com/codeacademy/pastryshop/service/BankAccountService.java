package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.exceptions.DepositException;
import com.codeacademy.pastryshop.model.BankAccount;
import com.codeacademy.pastryshop.model.Company;
import com.codeacademy.pastryshop.model.User;

import java.math.BigDecimal;

public interface BankAccountService {

  BankAccount openNewBankAccount(BigDecimal balance, BankAccount.Currency currency, Company businessUser);

  boolean cancelBankAccount(User businessUser, BankAccount account);

  BankAccount getBankAccountById(Long id);


  boolean deposit(BankAccount account, BigDecimal value) throws DepositException;

  boolean withdraw(BankAccount account, BigDecimal value);

  boolean changeCurrency(BankAccount account, BankAccount.Currency currency);

  BigDecimal exchangeMoneyCurrency(BigDecimal sum, BankAccount.Currency newCurrency);

  boolean transferMoney(BankAccount bankAccount, BigDecimal value , BankAccount bankAccountTarget);

  void updateBankAccountList(BankAccount bankAccount, Company user);

  boolean checkIfInMyList(Company company, BankAccount bankAccount);
}
