package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.model.BankAccount;
import com.codeacademy.pastryshop.model.Contract;

public interface ShopManagementService
{
  void splitDailyIncome(BankAccount workAccount, BankAccount rentAccount, Contract rentContract);
}
