package com.codeacademy.pastryshop.controller;

import com.codeacademy.pastryshop.dto.BusinessEntityDto;
import com.codeacademy.pastryshop.dto.BusinessWithIdDto;
import com.codeacademy.pastryshop.model.BusinessEntity;
import com.codeacademy.pastryshop.model.Company;
import com.codeacademy.pastryshop.service.BusinessServiceImpl;
import com.codeacademy.pastryshop.service.CompanyServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/v1/business")
@Validated
public class BusinessController
{
  private final CompanyServiceImpl companyService;

  private final BusinessServiceImpl businessService;

  @Autowired
  public BusinessController(CompanyServiceImpl companyService, BusinessServiceImpl businessService)
  {
    this.companyService = companyService;
    this.businessService = businessService;
  }

  @PostMapping("/open")
  @ResponseBody
  public ResponseEntity<HttpStatus> openNewBusiness(Principal principal, @RequestParam(value = "name") @Valid final String name, @RequestParam(value = "type")final String type)
  {
    Company user = (Company) companyService.findCompanyByUsername(principal.getName());
    BusinessEntityDto businessEntityDto;

    if (type.equals("shop"))
    {
      businessEntityDto = new BusinessEntityDto(BusinessEntity.Type.SHOP, name, Collections.emptyList());
      businessService.openBusiness(businessEntityDto, user);
    }
    if (type.equals("mall"))
    {
      businessEntityDto = new BusinessEntityDto(BusinessEntity.Type.MALL, name, Collections.emptyList());
      businessService.openBusiness(businessEntityDto, user);
    }
    if (type.equals("supplier"))
    {
      businessEntityDto = new BusinessEntityDto(BusinessEntity.Type.SUPPLIER, name, Collections.emptyList());
      businessService.openBusiness(businessEntityDto, user);
    }
    if(!type.equals("shop") && !type.equals("mall") && !type.equals("supplier"))
    {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PutMapping("/close")
  @ResponseBody
  public ResponseEntity<HttpStatus> closeMyBusiness(Principal principal,@RequestParam(value = "name") final String name)
  {
    Company user = (Company) companyService.findCompanyByUsername(principal.getName());

    BusinessEntity businessEntity = businessService.getBusinessByName(name);

    if (businessService.checkIfEntityIsOwnedByCompany(user, businessEntity))
    {
      return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }
    else
    {
      businessService.closeBusiness(businessEntity);
      return new ResponseEntity<>(HttpStatus.OK);
    }
  }

  @PutMapping()
  @ResponseBody
  public ResponseEntity<HttpStatus> changeMyBusinessName(Principal principal, @RequestParam(value = "name") final String name, @RequestParam(value = "newName") @Valid final String newName)
  {
    Company user = (Company) companyService.findCompanyByUsername(principal.getName());

    BusinessEntity businessEntity = businessService.getBusinessByName(name);

    if (businessService.checkIfEntityIsOwnedByCompany(user, businessEntity))
    {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    else
    {
      businessService.changeBusinessName(businessEntity, newName);
      return new ResponseEntity<>(HttpStatus.OK);
    }

  }

  @GetMapping()
  @ResponseBody
  public ResponseEntity<List<BusinessWithIdDto>> getMyBusinesses(Principal principal)
  {
    Company user = (Company) companyService.findCompanyByUsername(principal.getName());

    List<BusinessEntity> entityList = businessService.getAllEntitiesOfUser(user);
    List<BusinessWithIdDto> entityDtoList = new ArrayList<>();

    for (BusinessEntity b:entityList
         ) {
      BusinessWithIdDto entityDto = new BusinessWithIdDto(b.getId(),b.getType(), b.getName());
      entityDtoList.add(entityDto);
    }

    return new ResponseEntity<>(entityDtoList, HttpStatus.OK);
  }


}
