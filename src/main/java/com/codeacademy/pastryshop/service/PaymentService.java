package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.exceptions.FailedPaymentException;
import com.codeacademy.pastryshop.model.BankAccount;
import com.codeacademy.pastryshop.model.BankTransferHistory;
import com.codeacademy.pastryshop.model.BusinessEntity;
import com.codeacademy.pastryshop.model.Contract;

import java.math.BigDecimal;

public interface PaymentService
{
  boolean payRent(BusinessEntity shop, Contract contract, BankAccount shopBankAccount, BankAccount mallBankAccount) throws FailedPaymentException;

  boolean paySupplier(BusinessEntity businessEntity, Contract contract, BankAccount shopAccount, BankAccount supplierAccount) throws FailedPaymentException;

  boolean payShop(BusinessEntity shop, BankAccount clientAccount, BankAccount shopAccount, BigDecimal bill);

  void saveTransferLog(BankAccount from, BankAccount to, BigDecimal amount, boolean isSuccessful);

  boolean checkTimeBetweenPayments(BankTransferHistory lastTransfer, Integer amountOfDays);

  int checkIfSumAvailable(BankAccount bankAccount, Contract contract);

  //      payment with bank
  // side1 -> bank accounts
  // side2 -> bank account
  // try to find bank accounts with the same currency on side1 and side2
  // if found -> transfer between those bank account
  // else -> choose random bank account, convert currencies, do bank transfer

  // payment from client to owner
  // if (businessEntity.getType() == BusinessEntity.Type.SHOP) {
  // 30% go to euro bank account
  // 70% go to anywhere (BGN preferably)

}
