package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.exceptions.DepositException;
import com.codeacademy.pastryshop.exceptions.FailedPaymentException;
import com.codeacademy.pastryshop.model.BankAccount;
import com.codeacademy.pastryshop.model.Company;
import com.codeacademy.pastryshop.model.User;
import com.codeacademy.pastryshop.repository.BankAccountRepository;
import com.codeacademy.pastryshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService
{
  private static final BigDecimal exchangeRate = BigDecimal.valueOf(1.96);

  private final BankAccountRepository bankAccountRepository;


  private final UserRepository userRepository;

  @Autowired
  public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, UserRepository userRepository)
  {
    this.bankAccountRepository = bankAccountRepository;
    this.userRepository = userRepository;
  }

  @Override
  public BankAccount openNewBankAccount(BigDecimal balance, BankAccount.Currency currency, Company businessUser)
  {
    BankAccount bankAccount = new BankAccount(balance, currency, businessUser, true);
    bankAccountRepository.save(bankAccount);
    updateBankAccountList(bankAccount, businessUser);
    return bankAccount;
  }

  @Override
  public boolean cancelBankAccount(User businessUser, BankAccount account)
  {
    if (businessUser.getAccounts().contains(account)) {
      account.setActive(false);
      bankAccountRepository.save(account);
      return true;
    }
    else {
      return false;
    }
  }

  @Override
  public BankAccount getBankAccountById(Long id)
  {
    return bankAccountRepository.getBankAccountById(id);
  }


  @Override
  public boolean deposit(BankAccount account, BigDecimal value) throws DepositException
  {
    if (value.doubleValue() <= 0) {
      throw new DepositException("You cannot deposit negative values!");
    }
    account.setBalance(account.getBalance().add(value));
    bankAccountRepository.save(account);
    return true;
  }

  @Override
  public boolean withdraw(BankAccount account, BigDecimal value)
  {
    if (account.getBalance().compareTo(value) < 0) {
      //TODO: throw withdraw exception
    }
    else {
      account.setBalance(account.getBalance().subtract(value));
      bankAccountRepository.save(account);
      return true;
    }
    return false;
  }

  @Override
  public boolean changeCurrency(BankAccount account, BankAccount.Currency currency)
  {
    if (account.getCurrency() != currency) {
      account.setCurrency(currency);
      if (currency == BankAccount.Currency.BGN) {
        account.setBalance(account.getBalance().divide(exchangeRate));
      }
      else {
        account.setBalance(account.getBalance().multiply(exchangeRate));
      }
      return true;
    }
    return false;
  }

  @Override
  public BigDecimal exchangeMoneyCurrency(BigDecimal sum, BankAccount.Currency newCurrency)
  {
    if (newCurrency.equals(BankAccount.Currency.EUR)) {
      return sum.divide(exchangeRate);
    }
    else {
      return sum.multiply(exchangeRate);
    }
  }

  @Override
  public boolean transferMoney(BankAccount bankAccount, BigDecimal value, BankAccount targetAccount)
  {
    if (bankAccount.getBalance().compareTo(this.exchangeMoneyCurrency(value, bankAccount.getCurrency())) < 0) {
      throw new FailedPaymentException("Insufficient founds.");
    }
    if (bankAccount.getCurrency() != targetAccount.getCurrency()) {
      if (this.withdraw(bankAccount, value)) {
        value = this.exchangeMoneyCurrency(value, targetAccount.getCurrency());
        if (this.deposit(targetAccount, value)) {
          bankAccountRepository.save(bankAccount);
          bankAccountRepository.save(targetAccount);
          return true;
        }
      }
    }
    else {
      if (this.withdraw(bankAccount, value)) {
        if (this.deposit(targetAccount, value)) {
          bankAccountRepository.save(bankAccount);
          bankAccountRepository.save(targetAccount);
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public void updateBankAccountList(BankAccount bankAccount, Company user)
  {
    List<BankAccount> currentBankAccounts = user.getAccounts();

    currentBankAccounts.add(bankAccount);
    user.setAccounts(currentBankAccounts);

    userRepository.save(user);
  }

  @Override
  public boolean checkIfInMyList(Company company, BankAccount bankAccount)
  {
    return company.getAccounts().contains(bankAccount);
  }

}
