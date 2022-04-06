package com.bigtree.customer.repository;

import com.bigtree.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    @Query(nativeQuery = true, value = "SELECT * FROM customers WHERE contact ->> 'email' = :email")
    Customer findByEmail(@Param("email") String email);
}
