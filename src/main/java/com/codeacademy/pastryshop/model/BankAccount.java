package com.codeacademy.pastryshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_accounts")
public class BankAccount extends IdEntity
{

  @Column
  @PositiveOrZero
  private BigDecimal balance;

  @Column
  private Currency currency;

  @ManyToOne
  private User owner;

  @Column
  private boolean isActive;

  public enum Currency
  {
    EUR, BGN
  }

}
