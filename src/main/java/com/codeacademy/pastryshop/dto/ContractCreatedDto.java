package com.codeacademy.pastryshop.dto;

import com.codeacademy.pastryshop.model.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractCreatedDto
{
  private String sideAName;

  private BigDecimal scheduledPayment;

  private BankAccount.Currency currency;

  private LocalDate beginDate;

  private LocalDate expirationDate;
}
