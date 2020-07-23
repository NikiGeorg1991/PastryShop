package com.codeacademy.pastryshop.dto;

import com.codeacademy.pastryshop.model.BankAccount;
import com.codeacademy.pastryshop.model.BusinessEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.OneToOne;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContractParametersDto
{
  @Positive(message = "Scheduled payment cannot be negative!")
  private BigDecimal scheduledPayment;

  private BankAccount.Currency currency;

  @FutureOrPresent(message = "Begin date cannot be in the past!")
  private LocalDate beginDate;

  @Future(message = "Expiration date must be set in the future!")
  private LocalDate expirationDate;
}
