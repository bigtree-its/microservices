package com.bigtree.ecomm.orders.repository;

import com.bigtree.ecomm.orders.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@Repository
public interface InventoryRepository extends JpaRepository<Inventory, UUID> {

    /**
     * Retrieve <code>Inventory</code>s from the data store for given productId
     * @return a <code>Inventory</code>
     */
    @Transactional(readOnly = true)
    Inventory findByProductId(UUID productId);
}
