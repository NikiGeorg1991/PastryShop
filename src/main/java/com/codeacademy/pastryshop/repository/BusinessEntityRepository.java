package com.codeacademy.pastryshop.repository;

import com.codeacademy.pastryshop.model.BusinessEntity;
import com.codeacademy.pastryshop.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository

public interface BusinessEntityRepository extends JpaRepository<BusinessEntity, Long>
{


  List<BusinessEntity> findByOwner(Company company);


  BusinessEntity findByIdAndOwner(Long id, Company company);

  Optional<BusinessEntity> findByName(String name);
}
