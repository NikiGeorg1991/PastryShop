package com.codeacademy.pastryshop.repository;

import com.codeacademy.pastryshop.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long>
{

  BankAccount getBankAccountById(Long id);

//  BankAccount getBankAccountByIban(String iban);

}
