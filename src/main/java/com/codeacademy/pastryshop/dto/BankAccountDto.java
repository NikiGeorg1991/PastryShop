package com.codeacademy.pastryshop.dto;

import com.codeacademy.pastryshop.model.BankAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class BankAccountDto
{
  @PositiveOrZero
  private BigDecimal balance;

  @NotNull
  private BankAccount.Currency currency;

}
