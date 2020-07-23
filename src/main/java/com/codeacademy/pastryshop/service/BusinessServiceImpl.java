package com.codeacademy.pastryshop.service;

import com.codeacademy.pastryshop.dto.BusinessEntityDto;
import com.codeacademy.pastryshop.exceptions.NotCompanyException;
import com.codeacademy.pastryshop.exceptions.NotFoundException;
import com.codeacademy.pastryshop.model.BusinessEntity;
import com.codeacademy.pastryshop.model.Company;
import com.codeacademy.pastryshop.repository.BusinessEntityRepository;
import com.codeacademy.pastryshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BusinessServiceImpl implements BusinessService
{
  private final BusinessEntityRepository businessEntityRepository;

  private final UserRepository userRepository;

  @Autowired
  public BusinessServiceImpl(BusinessEntityRepository businessEntityRepository, UserRepository userRepository)
  {
    this.businessEntityRepository = businessEntityRepository;
    this.userRepository = userRepository;
  }

  @Override
  public BusinessEntity openBusiness(BusinessEntityDto businessEntityDto, Company company) throws NotCompanyException
  {
    if (checkIfIsCompany(company)) {
      BusinessEntity businessEntity = new BusinessEntity(businessEntityDto.getType(), businessEntityDto.getName(), company, true);
      businessEntityRepository.save(businessEntity);
      return businessEntity;
    }
    else
      throw new NotCompanyException("You are not a company!");

  }

  @Override
  public void closeBusiness(BusinessEntity businessEntity)
  {
    businessEntity.setOpen(false);
    businessEntityRepository.save(businessEntity);
  }

  @Override
  public void changeBusinessName(BusinessEntity businessEntity, String name)
  {
    businessEntity.setName(name);
    businessEntityRepository.save(businessEntity);
  }

  @Override
  public BusinessEntity getBusinessByID(Long id) throws NotFoundException
  {
    Optional<BusinessEntity> businessEntity = Optional.of(businessEntityRepository.getOne(id));

    return businessEntity.get();

  }

  @Override
  public BusinessEntity getBusinessByName(String name) throws NotFoundException
  {
    Optional<BusinessEntity> businessEntity = businessEntityRepository.findByName(name);
    if (businessEntity.isPresent()) {
      return businessEntity.get();
    }
    else {
      throw new NotFoundException("Not business found with the given name " + name);
    }
  }

  @Override
  public boolean checkIfIsCompany(Company user)
  {
    return user.getBulstat() != null;
  }

  @Override
  public void changeBulstat(Company company, String bulstat)
  {
    company.setBulstat(bulstat);
    userRepository.save(company);
  }

  @Override
  public boolean checkIfEntityIsOwnedByCompany(Company company, BusinessEntity businessEntity)
  {
    return businessEntityRepository.findByIdAndOwner(businessEntity.getId(), company) == null;
  }

  @Override
  public List<BusinessEntity> getAllEntitiesOfUser(Company company)
  {
    List<BusinessEntity> entities = businessEntityRepository.findByOwner(company);
    if (entities == null) {
      List<BusinessEntity> entityList = new ArrayList<>();
      return entityList;
    }
    else {
      return entities;
    }
  }
}
