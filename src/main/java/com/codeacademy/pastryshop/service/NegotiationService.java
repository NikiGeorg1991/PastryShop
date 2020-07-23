package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.dto.ContractParametersDto;
import com.codeacademy.pastryshop.exceptions.ContractMismatchException;
import com.codeacademy.pastryshop.model.BusinessEntity;
import com.codeacademy.pastryshop.model.Contract;

import java.time.LocalDate;

public interface NegotiationService
{
  Contract postContract(BusinessEntity businessEntity, ContractParametersDto contractParametersDto)  throws ContractMismatchException;

  void acceptRentContract(BusinessEntity businessEntity, Contract contract) throws ContractMismatchException;

  void acceptSupplyContract(BusinessEntity businessEntity, Contract contract) throws ContractMismatchException;

  boolean checkDate(LocalDate beginDate, LocalDate expirationDate);


}
