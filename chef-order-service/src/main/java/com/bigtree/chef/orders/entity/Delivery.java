package com.bigtree.chef.orders.entity;

import javax.persistence.*;

import com.bigtree.chef.orders.model.enums.DeliveryStatus;

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
@Table(name = "delivery")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@NamedNativeQueries({
        @NamedNativeQuery(name = "EstimatedCollectionDate",
                query = "SELECT d FROM Delivery d WHERE d.estimatedCollectionDate = ?",
                resultClass = Delivery.class),
        @NamedNativeQuery(name = "EstimatedDeliveryDate",
                query = "SELECT d FROM Delivery d WHERE d.estimatedDeliveryDate = ?",
                resultClass = Delivery.class),
        @NamedNativeQuery(name = "CollectedDate",
                query = "SELECT d FROM Delivery d WHERE d.collectedDate = ?",
                resultClass = Delivery.class),
        @NamedNativeQuery(name = "DeliveredDate",
                query = "SELECT d FROM Delivery d WHERE d.deliveredDate = ?",
                resultClass = Delivery.class),
        @NamedNativeQuery(name = "CourierPickupDateTime",
                query = "SELECT d FROM Delivery d WHERE d.courierPickupDateTime = ?",
                resultClass = Delivery.class),
        @NamedNativeQuery(name = "CollectedDateBetween",
                query = "SELECT p FROM Payment p WHERE p.collectedDate BETWEEN :startDate AND :endDate",
                resultClass = Delivery.class),
        @NamedNativeQuery(name = "DeliveredDateBetween",
                query = "SELECT p FROM Payment p WHERE p.deliveredDate BETWEEN :startDate AND :endDate",
                resultClass = Delivery.class),
        @NamedNativeQuery(name = "EstimatedCollectionDateBetween",
                query = "SELECT p FROM Payment p WHERE p.estimatedCollectionDate BETWEEN :startDate AND :endDate",
                resultClass = Delivery.class),
        @NamedNativeQuery(name = "EstimatedDeliveryDateBetween",
                query = "SELECT p FROM Payment p WHERE p.estimatedDeliveryDate BETWEEN :startDate AND :endDate",
                resultClass = Delivery.class)})

public class Delivery extends BaseEntity {

    @JsonFormat(pattern = "dd/MMM/yyyy")
    private LocalDate estimatedDeliveryDate;

    @JsonFormat(pattern = "dd/MMM/yyyy")
    private LocalDate deliveredDate;

    @JsonFormat(pattern = "dd/MMM/yyyy")
    private LocalDateTime orderPickedUpByDeliveryPartner;

    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID deliveryPartnerId;

    @Column(nullable = false)
    private UUID trackingReference;

    @Column(nullable = false)
    private String customerEmail;

    @Column(nullable = false)
    private String customerMobile;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private BigDecimal deliveryPartnerCharge;
    private BigDecimal packageCharge;
    private BigDecimal vatCharge;
    private Integer vatPercentage;

}
