package com.codeacademy.pastryshop.dto;

import com.codeacademy.pastryshop.model.BusinessEntity;
import com.codeacademy.pastryshop.model.Contract;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class BusinessEntityDto
{
  private BusinessEntity.Type type;

  @NotBlank(message = "You must enter a name!")
  @Size(min = 5, max = 35, message = "Name has to be between 5-35 characters long!")
  private String name;

  private List<Contract> myContracts;
}
