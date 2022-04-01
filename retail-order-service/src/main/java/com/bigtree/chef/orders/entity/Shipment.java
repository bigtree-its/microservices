package com.bigtree.chef.orders.entity;

import javax.persistence.*;

import com.bigtree.chef.orders.model.enums.ShipmentStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "shipments")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedNativeQueries({
        @NamedNativeQuery(name = "EstimatedCollectionDate",
                query = "SELECT s FROM Shipment s WHERE s.estimatedCollectionDate = ?",
                resultClass = Shipment.class),
        @NamedNativeQuery(name = "EstimatedDeliveryDate",
                query = "SELECT s FROM Shipment s WHERE s.estimatedDeliveryDate = ?",
                resultClass = Shipment.class),
        @NamedNativeQuery(name = "CollectedDate",
                query = "SELECT s FROM Shipment s WHERE s.collectedDate = ?",
                resultClass = Shipment.class),
        @NamedNativeQuery(name = "DeliveredDate",
                query = "SELECT s FROM Shipment s WHERE s.deliveredDate = ?",
                resultClass = Shipment.class),
        @NamedNativeQuery(name = "CourierPickupDateTime",
                query = "SELECT s FROM Shipment s WHERE s.courierPickupDateTime = ?",
                resultClass = Shipment.class),
        @NamedNativeQuery(name = "CollectedDateBetween",
                query = "SELECT p FROM Payment p WHERE p.collectedDate BETWEEN :startDate AND :endDate",
                resultClass = Shipment.class),
        @NamedNativeQuery(name = "DeliveredDateBetween",
                query = "SELECT p FROM Payment p WHERE p.deliveredDate BETWEEN :startDate AND :endDate",
                resultClass = Shipment.class),
        @NamedNativeQuery(name = "EstimatedCollectionDateBetween",
                query = "SELECT p FROM Payment p WHERE p.estimatedCollectionDate BETWEEN :startDate AND :endDate",
                resultClass = Shipment.class),
        @NamedNativeQuery(name = "EstimatedDeliveryDateBetween",
                query = "SELECT p FROM Payment p WHERE p.estimatedDeliveryDate BETWEEN :startDate AND :endDate",
                resultClass = Shipment.class)})

public class Shipment extends BaseEntity {

    @JsonFormat(pattern = "dd/MMM/yyyy")
    private LocalDate estimatedCollectionDate;

    @JsonFormat(pattern = "dd/MMM/yyyy")
    private LocalDate collectedDate;

    @JsonFormat(pattern = "dd/MMM/yyyy")
    private LocalDate estimatedDeliveryDate;

    @JsonFormat(pattern = "dd/MMM/yyyy")
    private LocalDate deliveredDate;

    @JsonFormat(pattern = "dd/MMM/yyyy")
    private LocalDateTime courierPickupDateTime;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID courierId;

    @Column(nullable = false)
    private UUID trackingReference;

    @Column(nullable = false)
    private String customerEmail;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;

    private BigDecimal courierCharge;
    private BigDecimal packageCharge;
    private BigDecimal vatCharge;
    private Integer vatPercentage;

}
