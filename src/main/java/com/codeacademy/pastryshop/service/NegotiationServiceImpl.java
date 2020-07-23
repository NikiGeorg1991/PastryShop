package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.dto.ContractParametersDto;
import com.codeacademy.pastryshop.exceptions.ContractMismatchException;
import com.codeacademy.pastryshop.model.BusinessEntity;
import com.codeacademy.pastryshop.model.Contract;
import com.codeacademy.pastryshop.repository.BusinessEntityRepository;
import com.codeacademy.pastryshop.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class NegotiationServiceImpl implements NegotiationService
{
  private final ContractRepository contractRepository;

  private final ContractServiceImpl contractService;

  private final BusinessEntityRepository businessEntityRepository;


  @Autowired
  public NegotiationServiceImpl(ContractRepository contractRepository, ContractServiceImpl contractService, BusinessEntityRepository businessEntityRepository)
  {
    this.contractRepository = contractRepository;
    this.contractService = contractService;
    this.businessEntityRepository = businessEntityRepository;
  }

  @Override
  public Contract postContract(BusinessEntity businessEntity, ContractParametersDto contractParametersDto) throws ContractMismatchException
  {
    if (businessEntity.getType().equals(BusinessEntity.Type.SHOP)) {
      throw new ContractMismatchException("You are not allowed to post contracts!");
    }
    if (!checkDate(contractParametersDto.getExpirationDate(), contractParametersDto.getBeginDate())) {
      throw new ContractMismatchException("The expiration date cannot be before the begin date!");
    }
    Contract contract = new Contract(businessEntity, contractParametersDto.getScheduledPayment(),
        contractParametersDto.getCurrency(), contractParametersDto.getBeginDate(), contractParametersDto.getExpirationDate());

    contractRepository.save(contract);
    contractService.updateContractList(businessEntity, contract);
    return contract;
  }

  @Override
  public void acceptRentContract(BusinessEntity shop, Contract contract) throws ContractMismatchException
  {
    if (shop.getType().equals(BusinessEntity.Type.SHOP)) {
      contract.setShop(shop);
      contract.setIsActive(true);
      contractRepository.save(contract);
    }
    else {
      throw new ContractMismatchException("You are not allowed to accept that type of contracts!");
    }
  }

  @Override
  public void acceptSupplyContract(BusinessEntity businessEntity, Contract contract)
  {
  //TODO: If needed, to be done
  }

  @Override
  public boolean checkDate(LocalDate beginDate, LocalDate expirationDate)
  {
    return beginDate.isAfter(expirationDate);
  }


}
