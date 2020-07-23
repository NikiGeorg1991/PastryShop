package com.codeacademy.pastryshop.dto;

import com.codeacademy.pastryshop.model.BankAccount;
import com.codeacademy.pastryshop.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class GetBankAccountInfoDto
{
  private Long id;

  private BankAccount.Currency currency;

  private String owner;

  private BigDecimal balance;
}
