package com.bigtree.customer.repository;

import com.bigtree.customer.entity.CustomerAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CustomerAccountRepository extends JpaRepository<CustomerAccount, UUID> {

    @Query("SELECT a from CustomerAccount a where a.customerId = :customerId")
    CustomerAccount getAccountByUserId(@Param("customerId") UUID customerId);
}
