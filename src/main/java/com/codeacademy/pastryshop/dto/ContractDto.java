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
public class ContractDto
{
  private Long id;

  private String sideAName;

  private String sideBName;

  private BigDecimal scheduledPayment;

  private BankAccount.Currency currency;

  private LocalDate beginDate;

  private LocalDate expirationDate;
}
