package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.model.BankAccount;
import com.codeacademy.pastryshop.model.BankTransferHistory;
import com.codeacademy.pastryshop.model.Contract;
import com.codeacademy.pastryshop.repository.BankTransferRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.ListIterator;

@Service
public class ShopManagementServiceImpl implements ShopManagementService
{
  private final BankTransferRepository bankTransferRepository;

  private final BankAccountServiceImpl bankAccountService;

  public ShopManagementServiceImpl(BankTransferRepository bankTransferRepository, BankAccountServiceImpl bankAccountService)
  {
    this.bankTransferRepository = bankTransferRepository;
    this.bankAccountService = bankAccountService;
  }

  @Override
  public void splitDailyIncome(BankAccount workAccount, BankAccount rentAccount, Contract rentContract)
  {
    LocalDate today = LocalDate.now();
    List<BankTransferHistory> dailyTransfers = bankTransferRepository.getDailyTransfersToAccount(workAccount.getId(), today);
    BigDecimal dailyIncome = new BigDecimal(0);
    ListIterator<BankTransferHistory> iterator = dailyTransfers.listIterator();

    while (iterator.hasNext()) {
      dailyIncome.add(iterator.next().getAmount());
    }

    BigDecimal rentDeposit = (dailyIncome.multiply(BigDecimal.valueOf(30))).divide(BigDecimal.valueOf(100));

    //TODO make sure rentAcount is in EUR
    BigDecimal totalMoneySaved = rentAccount.getBalance().add(bankAccountService.exchangeMoneyCurrency(rentDeposit, rentAccount.getCurrency()));

    if (rentContract.getScheduledPayment().compareTo(totalMoneySaved) <= 0) {

      BigDecimal difference = totalMoneySaved.subtract(rentContract.getScheduledPayment());
      difference = bankAccountService.exchangeMoneyCurrency(difference, workAccount.getCurrency());
      rentDeposit = rentDeposit.subtract(difference);
      bankAccountService.transferMoney(workAccount, rentDeposit, rentAccount);
    }
    else if (rentContract.getScheduledPayment().compareTo(totalMoneySaved) >= 0) {
      bankAccountService.transferMoney(workAccount, rentDeposit, rentAccount);
    }
    else {
      //TODO: throw money saved Exception or something like that
    }
  }
}
