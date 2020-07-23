package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.model.BusinessEntity;
import com.codeacademy.pastryshop.model.Company;
import com.codeacademy.pastryshop.model.Contract;

import java.util.List;

public interface ContractService {


  void terminateContract(Contract contract);

  List<Contract> getMyContracts(Company company);

  Contract getContract(Long id);

  boolean checkIfInMyList(BusinessEntity businessEntity, Contract contract);

  void updateMyContracts(BusinessEntity businessEntity);

  void updateAllContracts();

  List<Contract> getAllContracts();

  void updateContractList(BusinessEntity businessEntity, Contract contract);
}
