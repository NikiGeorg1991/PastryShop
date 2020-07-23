package com.codeacademy.pastryshop.repository;

import com.codeacademy.pastryshop.model.Company;
import com.codeacademy.pastryshop.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {


  List<Contract> findContractsByShopOwner(Company company);

}
