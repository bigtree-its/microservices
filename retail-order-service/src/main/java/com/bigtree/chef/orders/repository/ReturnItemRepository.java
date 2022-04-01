package com.bigtree.chef.orders.repository;

import com.bigtree.chef.orders.entity.ReturnItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReturnItemRepository extends JpaRepository<ReturnItem, UUID> {
    
}
