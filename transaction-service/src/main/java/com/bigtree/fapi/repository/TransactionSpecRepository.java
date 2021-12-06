package com.bigtree.fapi.repository;

import com.bigtree.fapi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TransactionSpecRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {
}
