package com.codeacademy.pastryshop.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_contracts")
public class Contract extends IdEntity{

    @ManyToOne
    @JoinColumn(name = "sideA_id")
    private BusinessEntity sideA;

    @ManyToOne
    @JoinColumn(name = "shop_id")
    private BusinessEntity shop;

    @Column
    @PositiveOrZero
    private BigDecimal scheduledPayment;

    @Column
    private BankAccount.Currency currency;

    @Column
    private Boolean isActive;

    @Column
    @FutureOrPresent
    private LocalDate beginDate;

    @Column
    @FutureOrPresent
    private LocalDate expirationDate;

    public Contract(BusinessEntity sideA, @PositiveOrZero BigDecimal scheduledPayment, BankAccount.Currency currency, @FutureOrPresent LocalDate beginDate, @FutureOrPresent LocalDate expirationDate)
    {
        //Post contract constructor
        this.sideA = sideA;
        this.scheduledPayment = scheduledPayment;
        this.currency = currency;
        this.beginDate = beginDate;
        this.expirationDate = expirationDate;
        this.isActive = false;
    }
}
