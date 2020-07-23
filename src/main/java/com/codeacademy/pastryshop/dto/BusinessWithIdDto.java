package com.codeacademy.pastryshop.dto;

import com.codeacademy.pastryshop.model.BusinessEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class BusinessWithIdDto implements Serializable
{
  private Long id;

  private BusinessEntity.Type type;

  private String name;

}
