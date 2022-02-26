package com.bigtree.customer.repository;

import com.bigtree.customer.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

    @Query("SELECT a from Session a where a.customerId = :customerId")
    Session findByUserId(@Param("customerId") UUID customerId);
}
