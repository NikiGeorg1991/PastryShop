package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.exceptions.FailedPaymentException;
import com.codeacademy.pastryshop.model.BankAccount;
import com.codeacademy.pastryshop.model.BankTransferHistory;
import com.codeacademy.pastryshop.model.BusinessEntity;
import com.codeacademy.pastryshop.model.Contract;
import com.codeacademy.pastryshop.repository.BankAccountRepository;
import com.codeacademy.pastryshop.repository.BankTransferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService
{

  private final BankAccountRepository bankAccountRepository;

  private final BankTransferRepository bankTransferRepository;

  private final ContractServiceImpl contractService;

  private final BusinessServiceImpl businessService;

  private final BankAccountServiceImpl bankAccountService;

  @Autowired
  public PaymentServiceImpl(BankAccountRepository bankAccountRepository, BankTransferRepository bankTransferRepository, ContractServiceImpl contractService, BusinessServiceImpl businessService, BankAccountServiceImpl bankAccountService)
  {
    this.bankAccountRepository = bankAccountRepository;
    this.bankTransferRepository = bankTransferRepository;
    this.contractService = contractService;
    this.businessService = businessService;
    this.bankAccountService = bankAccountService;
  }

  @Override
  public boolean payRent(BusinessEntity shop, Contract contract, BankAccount shopAccount, BankAccount mallAccount) throws FailedPaymentException
  {
    if (!contractService.checkIfInMyList(shop, contract) && !bankAccountService.checkIfInMyList(shop.getOwner(), shopAccount)) {
      //TODO: Some exception
    }

    if (bankTransferRepository.getTransfers(shopAccount.getId(), mallAccount.getId()) == null) {
      // Since this will be the first transaction it will be successful now matter what
      bankAccountService.transferMoney(shopAccount, contract.getScheduledPayment(), mallAccount);
      saveTransferLog(shopAccount, mallAccount, contract.getScheduledPayment(), true);
      return true;
    }

    List<BankTransferHistory> transfersBetween = bankTransferRepository.getTransfers(shopAccount.getId(), mallAccount.getId());
    transfersBetween.sort(Comparator.comparing(BankTransferHistory::getTimestamp).reversed());
    BankTransferHistory lastTransfer = transfersBetween.get(0);

    if (checkTimeBetweenPayments(lastTransfer, 30) && (checkIfSumAvailable(shopAccount, contract) == 0 || checkIfSumAvailable(shopAccount, contract) == 1)) {
      bankAccountService.transferMoney(shopAccount, contract.getScheduledPayment(), mallAccount);
      saveTransferLog(shopAccount, mallAccount, contract.getScheduledPayment(), true);
      return true;
    }
    else {
      saveTransferLog(shopAccount, mallAccount, contract.getScheduledPayment(), false);
      contractService.terminateContract(contract);
      businessService.closeBusiness(shop);
      throw new FailedPaymentException("....shop closed");
    }
  }

  @Override
  public boolean paySupplier(BusinessEntity shop, Contract contract, BankAccount shopAccount, BankAccount supplierAccount) throws FailedPaymentException
  {
    if (!contractService.checkIfInMyList(shop, contract) && !bankAccountService.checkIfInMyList(shop.getOwner(), shopAccount)) {
      //TODO: Some exception
    }

    if (bankTransferRepository.getTransfers(shopAccount.getId(), supplierAccount.getId()) == null) {
      // Since this will be the first transaction it will be successful now matter what
      bankAccountService.transferMoney(shopAccount, contract.getScheduledPayment(), supplierAccount);
      saveTransferLog(shopAccount, supplierAccount, contract.getScheduledPayment(), true);
      return true;
    }
    List<BankTransferHistory> transfersBetween = bankTransferRepository.getTransfers(shopAccount.getId(), supplierAccount.getId());
    transfersBetween.sort(Comparator.comparing(BankTransferHistory::getTimestamp).reversed());
    BankTransferHistory lastTransfer = transfersBetween.get(0);

    if (checkTimeBetweenPayments(lastTransfer, 7) && (checkIfSumAvailable(shopAccount, contract) == 0 || checkIfSumAvailable(shopAccount, contract) == 1)) {
      bankAccountService.transferMoney(shopAccount, contract.getScheduledPayment(), supplierAccount);
      saveTransferLog(shopAccount, supplierAccount, contract.getScheduledPayment(), true);
      return true;
    }
    else {
      saveTransferLog(shopAccount, supplierAccount, contract.getScheduledPayment(), false);
      contractService.terminateContract(contract);
      throw new FailedPaymentException("....contract terminated");
    }
  }

  @Override
  public void saveTransferLog(BankAccount from, BankAccount to, BigDecimal amount, boolean isSuccessful)
  {
    BankTransferHistory bankTransfer = new BankTransferHistory(from, to, amount, LocalDate.now(), isSuccessful);
    bankTransferRepository.save(bankTransfer);
  }

  @Override
  public boolean checkTimeBetweenPayments(BankTransferHistory lastTransfer, Integer amountOfDays)
  {
    Duration duration = Duration.between(lastTransfer.getTimestamp(), LocalDate.now());

    return duration.toDays() <= amountOfDays;
  }

  @Override
  public int checkIfSumAvailable(BankAccount bankAccount, Contract contract)
  {
    return bankAccount.getBalance().compareTo(contract.getScheduledPayment());
  }

  @Override
  public boolean payShop(BusinessEntity shop, BankAccount clientAccount, BankAccount shopAccount, BigDecimal bill)
  {
    if (shop.isOpen()) {
      bankAccountService.transferMoney(clientAccount, bill, shopAccount);
      saveTransferLog(clientAccount, shopAccount, bill, true);
      return true;
    }
    throw new FailedPaymentException("Shop is currently closed for business.");
  }

}
