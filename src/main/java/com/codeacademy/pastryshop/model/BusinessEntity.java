package com.codeacademy.pastryshop.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tbl_business")
public class BusinessEntity extends IdEntity{

    public enum Type {
        MALL,
        SHOP,
        SUPPLIER
    }

    @Column
    private Type type;

    @Column
    @NotNull
    private String name;

    @OneToMany
    private List<Contract> myContracts;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "owner_id")
    private Company owner;

    @NotNull
    boolean isOpen;


    public BusinessEntity(Type type, @NotNull String name, @NotNull Company owner, @NotNull boolean isOpen)
    {
        this.type = type;
        this.name = name;
        this.owner = owner;
        this.isOpen = isOpen;
        this.myContracts = new ArrayList<>();
    }

}
