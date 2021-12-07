package com.bigtree.fapi.repository;

import com.bigtree.fapi.entity.Account;
import com.bigtree.fapi.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    @Query("SELECT a from Company a where a.id = :companyId")
    Account getCompany(@Param("companyId") UUID companyId);

    @Query("SELECT a from Company a where a.number = :number")
    Company findByCompanyNumber(@Param("number") String number);
}
