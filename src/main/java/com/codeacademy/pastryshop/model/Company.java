package com.codeacademy.pastryshop.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Company extends User
{

  @Column
  private String bulstat;


  public Company(String username, String password, String firstName, String lastName,
                 String email,
                 String phoneNumber, String bulstat)
  {
    super(username, password, firstName, lastName, email, phoneNumber);
    this.bulstat = bulstat;
  }

  public Company(String bulstat)
  {
    this.bulstat = bulstat;
  }
}
