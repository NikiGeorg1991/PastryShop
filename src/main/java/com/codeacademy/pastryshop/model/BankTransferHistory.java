package com.codeacademy.pastryshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_transfers")
public class BankTransferHistory extends IdEntity
{
  @OneToOne
  BankAccount from;

  @OneToOne
  BankAccount to;

  BigDecimal  amount;

  LocalDate   timestamp;

  // http://exchangeratesapi.io

  boolean    isSucessfull;

}
