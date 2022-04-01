package com.bigtree.chef.orders.repository;

import com.bigtree.chef.orders.entity.Return;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderReturnRepository extends JpaRepository<Return, UUID> {
    
}
