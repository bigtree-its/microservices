package com.bigtree.chef.orders.repository;

import com.bigtree.chef.orders.entity.Delivery;
import com.bigtree.chef.orders.entity.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    @Transactional(readOnly = true)
    @Query("SELECT d FROM Delivery d WHERE d.orderId = ?1")
    List<Delivery> findByOrderId(@Param("orderId") UUID orderId);

    @Transactional(readOnly = true)
    @Query("SELECT d FROM Delivery d WHERE d.customerEmail = ?1")
    List<Delivery> findByCustomerEmail(@Param("customerEmail") String customerEmail);

    @Transactional(readOnly = true)
    @Query("SELECT d FROM Delivery d WHERE d.customerMobile = ?1")
    List<Delivery> findByCustomerMobile(@Param("customerMobile") String customerMobile);

    @Transactional(readOnly = true)
    @Query("SELECT d FROM Delivery d WHERE d.deliveryPartnerId = ?1")
    List<Delivery> findByCourierId(@Param("deliveryPartnerId") UUID deliveryPartnerId);

    @Transactional(readOnly = true)
    @Query("SELECT d FROM Delivery d WHERE d.orderPickedUpByDeliveryPartner = ?1")
    List<Delivery> findByOrderPickedUpByDeliveryPartner(@Param("orderPickedUpByDeliveryPartner") LocalDateTime orderPickedUpByDeliveryPartner);

    @Transactional(readOnly = true)
    @Query("SELECT d FROM Delivery d WHERE d.estimatedDeliveryDate = ?1")
    List<Delivery> findByEstimatedDeliveryDate(@Param("estimatedDeliveryDate") LocalDate estimatedDeliveryDate);

    @Transactional(readOnly = true)
    @Query("SELECT d FROM Delivery d WHERE d.deliveredDate = ?1")
    List<Delivery> findByDeliveredDate(@Param("deliveredDate") LocalDateTime deliveredDate);

    @Transactional(readOnly = true)
    @Query("SELECT d FROM Delivery d WHERE d.trackingReference = ?1")
    Delivery findByTrackingReference(@Param("trackingReference") UUID trackingReference);

    @Transactional(readOnly = true)
    @Query("SELECT d FROM Delivery d WHERE d.status = ?1")
    List<Delivery> findByStatus(@Param("status") String status);

    @Transactional(readOnly = true)
    @Query("SELECT d FROM Delivery d WHERE d.deliveredDate BETWEEN :startDate AND :endDate")
    List<Payment> findByDeliveredDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Transactional(readOnly = true)
    @Query("SELECT d FROM Delivery d WHERE d.estimatedDeliveryDate BETWEEN :startDate AND :endDate")
    List<Payment> findByEstimatedDeliveryDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
