package com.codeacademy.pastryshop.repository;

import com.codeacademy.pastryshop.model.BankTransferHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BankTransferRepository extends JpaRepository<BankTransferHistory, String>
{
  @Query(value = "SELECT * FROM tbl_transfers t WHERE t.from = :from AND t.to = :to", nativeQuery = true)
  List<BankTransferHistory> getTransfers(@Param("from") Long from, @Param("to") Long to);

  @Query(value = "SELECT * FROM tbl_transfers t WHERE t.to = :to AND t.timestamp = :today", nativeQuery = true)
  List<BankTransferHistory> getDailyTransfersToAccount(@Param("to") Long to, @Param("today")LocalDate today);
}
