package com.bigtree.chef.orders.repository;

import com.bigtree.chef.orders.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;


@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    /**
     * Retrieve <code>Payments</code>s from the data store for given email
     * @return a <code>Payment</code>
     */
    @Transactional(readOnly = true)
    @Query("SELECT p FROM Payment p WHERE p.customerEmail = ?1")
    List<Payment> findByCustomerEmail(@Param("customerEmail")  String customerEmail);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM Payment p WHERE p.stripeReference = ?1")
    Payment findByStripeReference(@Param("stripeReference")  String stripeReference);

    /**
     * Retrieve <code>Payments</code>s from the data store for given orderId
     * @return a <code>Payment</code>
     */
    @Transactional(readOnly = true)
    @Query("SELECT p FROM Payment p WHERE p.orderId = ?1")
    Payment findByOrderId(@Param("orderId")  UUID orderId);

    /**
     * Retrieve <code>Payments</code> by status
     * @return a <code>Payment</code>
     */
    @Transactional(readOnly = true)
    @Query("SELECT p FROM Payment p WHERE p.status = ?1")
    List<Payment> findByStatus(@Param("status")  String status);

    /**
     * Retrieve <code>Payments</code> by period
     * @return a <code>Payment</code>
     */
    @Transactional(readOnly = true)
    @Query("SELECT p FROM Payment p WHERE p.date BETWEEN :startDate AND :endDate")
    List<Payment> findByDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Transactional(readOnly = true)
    @Query("SELECT p FROM Payment p WHERE p.date = ?1")
    List<Payment> findByDate(@Param("date") LocalDate date);

}
