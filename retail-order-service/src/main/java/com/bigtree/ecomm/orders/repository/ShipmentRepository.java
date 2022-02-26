package com.bigtree.ecomm.orders.repository;

import com.bigtree.ecomm.orders.entity.Payment;
import com.bigtree.ecomm.orders.entity.Shipment;

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
public interface ShipmentRepository extends JpaRepository<Shipment, UUID> {

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shipment s WHERE s.orderId = ?1")
    List<Shipment> findByOrderId(@Param("orderId") UUID orderId);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shipment s WHERE s.customerEmail = ?1")
    List<Shipment> findByCustomerEmail(@Param("customerEmail") String customerEmail);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shipment s WHERE s.courierId = ?1")
    List<Shipment> findByCourierId(@Param("status") UUID courierId);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shipment s WHERE s.collectedDate = ?1")
    List<Shipment> findByCollectedDate(@Param("collectedDate") LocalDate collectedDate);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shipment s WHERE s.estimatedCollectionDate = ?1")
    List<Shipment> findByEstimatedCollectionDate(@Param("estimatedCollectionDate") LocalDate estimatedCollectionDate);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shipment s WHERE s.courierPickupDateTime = ?1")
    List<Shipment> findByCourierPickupDateTime(@Param("courierPickupDateTime") LocalDateTime courierPickupDateTime);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shipment s WHERE s.estimatedDeliveryDate = ?1")
    List<Shipment> findByEstimatedDeliveryDate(@Param("estimatedDeliveryDate") LocalDate estimatedDeliveryDate);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shipment s WHERE s.deliveredDate = ?1")
    List<Shipment> findByDeliveredDate(@Param("deliveredDate") LocalDateTime deliveredDate);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shipment s WHERE s.trackingReference = ?1")
    Shipment findByTrackingReference(@Param("trackingReference") UUID trackingReference);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shipment s WHERE s.status = ?1")
    List<Shipment> findByStatus(@Param("status") String status);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shipment s WHERE s.deliveredDate BETWEEN :startDate AND :endDate")
    List<Payment> findByDeliveredDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shipment s WHERE s.collectedDate BETWEEN :startDate AND :endDate")
    List<Payment> findByCollectedDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shipment s WHERE s.estimatedCollectionDate BETWEEN :startDate AND :endDate")
    List<Payment> findByEstimatedCollectionDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Transactional(readOnly = true)
    @Query("SELECT s FROM Shipment s WHERE s.estimatedDeliveryDate BETWEEN :startDate AND :endDate")
    List<Payment> findByEstimatedDeliveryDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
