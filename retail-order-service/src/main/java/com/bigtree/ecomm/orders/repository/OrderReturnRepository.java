package com.bigtree.ecomm.orders.repository;

import com.bigtree.ecomm.orders.entity.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderReturnRepository extends JpaRepository<Return, UUID> {
    
}
