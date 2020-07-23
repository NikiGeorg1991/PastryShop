package com.codeacademy.pastryshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User extends IdEntity {

    @Column
    @NotNull
    private String username;

    @Column
    @NotNull
    private String password;

    @Column(updatable = false)
    private String firstName;

    @Column(updatable = false)
    private String lastName;

    @Column
    @Email
    private String email;

    @Column
    private String phoneNumber;

    @OneToMany
    private List<BankAccount> accounts;


    public User(@NotNull String username, @NotNull String password, String firstName, String lastName, @Email String email, @NotBlank String phoneNumber)
    {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.accounts = new ArrayList<>();
    }
}
