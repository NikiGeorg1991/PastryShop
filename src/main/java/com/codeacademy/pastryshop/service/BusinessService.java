package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.dto.BusinessEntityDto;
import com.codeacademy.pastryshop.exceptions.NotCompanyException;
import com.codeacademy.pastryshop.model.BusinessEntity;
import com.codeacademy.pastryshop.model.Company;

import java.util.List;

public interface BusinessService
{

  BusinessEntity openBusiness(BusinessEntityDto businessEntityDto, Company company) throws NotCompanyException;

  void closeBusiness(BusinessEntity businessEntity);

  void changeBusinessName(BusinessEntity businessEntity, String name);

  BusinessEntity getBusinessByID(Long id) throws Exception;

  BusinessEntity getBusinessByName(String name) throws Throwable;

  boolean checkIfIsCompany(Company user);

  void changeBulstat(Company company, String bulstat);

  boolean checkIfEntityIsOwnedByCompany(Company company, BusinessEntity businessEntity);

  List<BusinessEntity> getAllEntitiesOfUser(Company company);
}
