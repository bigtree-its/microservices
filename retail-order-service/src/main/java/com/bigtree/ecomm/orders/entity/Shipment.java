package com.bigtree.ecomm.orders.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.bigtree.ecomm.orders.entity.BaseEntity;
import com.bigtree.ecomm.orders.model.enums.ShipmentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Entity
@Table(name = "shipments")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shipment extends BaseEntity {
    
    @Column(nullable = false)
    private UUID orderId;

    @Column(nullable = false)
    private UUID courierId;

    @Column(nullable = false)
    private UUID trackingReference;

    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;
    
}
