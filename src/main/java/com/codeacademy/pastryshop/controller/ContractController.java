package com.codeacademy.pastryshop.controller;

import com.codeacademy.pastryshop.dto.ContractCreatedDto;
import com.codeacademy.pastryshop.dto.ContractDto;
import com.codeacademy.pastryshop.dto.ContractParametersDto;
import com.codeacademy.pastryshop.exceptions.ContractMismatchException;
import com.codeacademy.pastryshop.model.BusinessEntity;
import com.codeacademy.pastryshop.model.Company;
import com.codeacademy.pastryshop.model.Contract;
import com.codeacademy.pastryshop.service.BusinessServiceImpl;
import com.codeacademy.pastryshop.service.CompanyServiceImpl;
import com.codeacademy.pastryshop.service.ContractServiceImpl;
import com.codeacademy.pastryshop.service.NegotiationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contracts")
@Validated
public class ContractController
{
  private final BusinessServiceImpl businessService;

  private final CompanyServiceImpl userService;

  private final NegotiationServiceImpl negotiationService;

  private final ContractServiceImpl contractService;

  @Autowired
  public ContractController(BusinessServiceImpl businessService, CompanyServiceImpl userService, NegotiationServiceImpl negotiationService, ContractServiceImpl contractService)
  {
    this.businessService = businessService;
    this.userService = userService;
    this.negotiationService = negotiationService;
    this.contractService = contractService;
  }

  @PostMapping("/post/{id}")
  @ResponseBody
  public ResponseEntity<ContractCreatedDto> publishContract(@PathVariable(value = "id") final Long id, @Valid @RequestBody ContractParametersDto contract, Principal principal) throws ContractMismatchException
  {
    Company company = (Company) userService.findCompanyByUsername(principal.getName());
    BusinessEntity businessEntity = businessService.getBusinessByID(id);

    if (businessService.checkIfEntityIsOwnedByCompany(company, businessEntity)) {
      return new ResponseEntity<>(new ContractCreatedDto(),HttpStatus.BAD_REQUEST);
    }
    else {
      Contract newContract = negotiationService.postContract(businessEntity, contract);
      ContractCreatedDto createdDto = new ContractCreatedDto(newContract.getSideA().getName(), newContract.getScheduledPayment()
      ,newContract.getCurrency(), newContract.getBeginDate(),newContract.getExpirationDate());
      return new ResponseEntity<>(createdDto,HttpStatus.CREATED);
    }
  }

  @PutMapping("/my-contracts/terminate/{id}")
  @ResponseBody
  public ResponseEntity<HttpStatus> closeContract(@PathVariable(value = "id") Long id, Principal principal)
  {
    Company company = (Company) userService.findCompanyByUsername(principal.getName());
    Contract contract = contractService.getContract(id);

    if (businessService.checkIfEntityIsOwnedByCompany(company, contract.getSideA()) ||
        businessService.checkIfEntityIsOwnedByCompany(company, contract.getShop())) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    else {
      contractService.terminateContract(contract);
      return new ResponseEntity<>(HttpStatus.OK);
    }
  }

  @GetMapping("/{id}")
  @ResponseBody
  public ResponseEntity<ContractCreatedDto> getContract(@PathVariable(value = "id") Long id)
  {
    Contract contract = contractService.getContract(id);
    if (contract == null) {
      return new ResponseEntity<>(new ContractCreatedDto(), HttpStatus.NOT_FOUND);
    }
    else {
      ContractCreatedDto contractDto = new ContractCreatedDto(contract.getSideA().getName(), contract.getScheduledPayment()
          ,contract.getCurrency(), contract.getBeginDate(),contract.getExpirationDate());
      return new ResponseEntity<>(contractDto, HttpStatus.OK);
    }

  }


  @GetMapping("/my-contracts/{id}")
  @ResponseBody
  public ResponseEntity<ContractDto> getMyContract(@PathVariable(value = "id") Long id, Principal principal)
  {
    Company company = (Company) userService.findCompanyByUsername(principal.getName());
    Contract contract = contractService.getContract(id);

    if (contract == null) {
      return new ResponseEntity<>(new ContractDto(), HttpStatus.NOT_FOUND);
    }

    if (businessService.checkIfEntityIsOwnedByCompany(company, contract.getSideA()) ||
        businessService.checkIfEntityIsOwnedByCompany(company, contract.getShop())) {
      return new ResponseEntity<>(new ContractDto(), HttpStatus.BAD_REQUEST);
    }
    else {
      ContractDto contractDto = new ContractDto(contract.getId(),contract.getSideA().getName(), contract.getShop().getName(), contract.getScheduledPayment(),
          contract.getCurrency(), contract.getBeginDate(), contract.getExpirationDate());
      return new ResponseEntity<>(contractDto, HttpStatus.OK);
    }
  }

  @GetMapping()
  @ResponseBody
  public ResponseEntity<List<ContractCreatedDto>> getAllContracts()
  {
    List<Contract> contracts = contractService.getAllContracts();

    List<ContractCreatedDto> contractsDto = new ArrayList<>();
    for (Contract c : contracts
    ) {
      ContractCreatedDto contractDto = new ContractCreatedDto(c.getSideA().getName(), c.getScheduledPayment()
          ,c.getCurrency(), c.getBeginDate(),c.getExpirationDate());

      contractsDto.add(contractDto);
    }
    return new ResponseEntity<>(contractsDto, HttpStatus.OK);
  }


  @GetMapping("/my-contracts")
  @ResponseBody
  public ResponseEntity<List<ContractDto>> getMyContracts(Principal principal)
  {
    Company company = (Company) userService.findCompanyByUsername(principal.getName());

    List<Contract> contractList = contractService.getMyContracts(company);
    List<ContractDto> contractDtos = new ArrayList<>();
    for (Contract c : contractList
    ) {
      ContractDto contractDto = new ContractDto(c.getId(),c.getSideA().getName(), c.getShop().getName(),
          c.getScheduledPayment(), c.getCurrency(), c.getBeginDate(), c.getExpirationDate());
      contractDtos.add(contractDto);
    }
    return new ResponseEntity<>(contractDtos, HttpStatus.OK);
  }

  @PostMapping("/accept/{id}")
  @ResponseBody
  public ResponseEntity<ContractDto> acceptContract(@PathVariable(value = "id") final Long id, @RequestParam(value = "name") final String name, Principal principal) throws ContractMismatchException
  {
    Company company = (Company) userService.findCompanyByUsername(principal.getName());
    BusinessEntity businessEntity = businessService.getBusinessByName(name);
    Contract contract = contractService.getContract(id);

    if (businessService.checkIfEntityIsOwnedByCompany(company, businessEntity)) {
      return new ResponseEntity<>(new ContractDto(), HttpStatus.BAD_REQUEST);
    }
    else {
      negotiationService.acceptRentContract(businessEntity, contract);
      ContractDto contractDto = new ContractDto(contract.getId(),contract.getSideA().getName(), contract.getShop().getName(),
          contract.getScheduledPayment(), contract.getCurrency(), contract.getBeginDate(), contract.getExpirationDate());
      return new ResponseEntity<>(contractDto, HttpStatus.OK);
    }
  }

  @GetMapping("/my-contracts/business/{id}")
  @ResponseBody
  public ResponseEntity<List<ContractDto>> getContractsOfBusiness(@PathVariable(value = "id") final Long id, Principal principal)
  {
    Company company = (Company) userService.findCompanyByUsername(principal.getName());
    BusinessEntity businessEntity = businessService.getBusinessByID(id);
    List<Contract> contractList = businessEntity.getMyContracts();

    List<ContractDto> contractDtos = new ArrayList<>();

    if (businessService.checkIfEntityIsOwnedByCompany(company, businessEntity)) {
      return new ResponseEntity<>(contractDtos, HttpStatus.BAD_REQUEST);
    }
    else {
      for (Contract c : contractList
      ) {
        ContractDto contractDto = new ContractDto(c.getId(),c.getSideA().getName(), "",
            c.getScheduledPayment(), c.getCurrency(), c.getBeginDate(), c.getExpirationDate());

        contractDtos.add(contractDto);
      }
      return new ResponseEntity<>(contractDtos, HttpStatus.OK);
    }
  }

}
