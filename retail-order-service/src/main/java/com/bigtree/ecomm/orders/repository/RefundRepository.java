package com.bigtree.ecomm.orders.repository;

import com.bigtree.ecomm.orders.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;


@Repository
public interface RefundRepository extends JpaRepository<Refund, UUID> {

    /**
     * Retrieve <code>Refunds</code>s from the data store for given email
     * @return a <code>Refund</code>
     */
    @Transactional(readOnly = true)
    @Query("SELECT p FROM Refund p WHERE p.customerEmail = ?1")
    List<Refund> findByCustomerEmail(@Param("customerEmail") String customerEmail);

    /**
     * Retrieve <code>Refunds</code>s from the data store for given orderId
     * @return a <code>Refund</code>
     */
    @Transactional(readOnly = true)
    @Query("SELECT p FROM Refund p WHERE p.orderId = ?1")
    Refund findByOrderId(UUID orderId);

    /**
     * Retrieve <code>Refunds</code> by status
     * @return a <code>Refund</code>
     */
    @Transactional(readOnly = true)
    @Query("SELECT p FROM Refund p WHERE p.status = ?1")
    List<Refund> findByStatus(String status);

    /**
     * Retrieve <code>Refunds</code> by period
     * @return a <code>Refund</code>
     */
    @Transactional(readOnly = true)
    @Query("SELECT p FROM Refund p WHERE p.date BETWEEN :startDate AND :endDate")
    List<Refund> findByDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
