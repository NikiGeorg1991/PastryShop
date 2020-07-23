package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.model.BusinessEntity;
import com.codeacademy.pastryshop.model.Company;
import com.codeacademy.pastryshop.model.Contract;
import com.codeacademy.pastryshop.repository.BusinessEntityRepository;
import com.codeacademy.pastryshop.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ContractServiceImpl implements ContractService
{
  private final ContractRepository contractRepository;

  private final BusinessEntityRepository businessEntityRepository;


  @Autowired
  public ContractServiceImpl(ContractRepository contractRepository, BusinessEntityRepository businessEntityRepository)
  {
    this.contractRepository = contractRepository;
    this.businessEntityRepository = businessEntityRepository;
  }

  @Override
  public void terminateContract(Contract contract)
  {
    contract.setIsActive(false);
    contractRepository.save(contract);
    updateContractList(contract.getShop(),contract);
    updateContractList(contract.getSideA(), contract);
  }

  @Override
  public List<Contract> getMyContracts(Company company)
  {
    return contractRepository.findContractsByShopOwner(company);
  }

  @Override
  public Contract getContract(Long id)
  {
    Optional<Contract> contract = contractRepository.findById(id);
    return contract.orElseGet(Contract::new);
  }

  @Override
  public boolean checkIfInMyList(BusinessEntity businessEntity, Contract contract)
  {
    return businessEntity.getMyContracts().contains(contract);
  }

  @Override
  public void updateMyContracts(BusinessEntity businessEntity)
  {
      List<Contract> myCurrentContracts = businessEntity.getMyContracts();
      for (Contract c : myCurrentContracts
      ) {
        if (c.getExpirationDate().isAfter(LocalDate.now())) {
          c.setIsActive(false);
          updateContractList(c.getSideA(), c);
        }
    }
  }

  @Override
  @Scheduled(cron = "0 0 12 */7 * ?")
  public void updateAllContracts()
  {
    List<Contract> allContracts = contractRepository.findAll();
    for (Contract c : allContracts
    ) {
      if (c.getShop() == null && !(c.getBeginDate().isEqual(LocalDate.now()))) {
        contractRepository.delete(c);
      }
    }
  }

  @Override
  public List<Contract> getAllContracts()
  {
    List<Contract> allContracts = contractRepository.findAll();

    allContracts.removeIf(Contract::getIsActive);
    return allContracts;
  }


  @Override
  public void updateContractList(BusinessEntity businessEntity, Contract contract)
  {
    List<Contract> myCurrentContracts = businessEntity.getMyContracts();
    myCurrentContracts.add(contract);
    businessEntity.setMyContracts(myCurrentContracts);
    businessEntityRepository.save(businessEntity);
  }
}
