package com.bigtree.chef.orders.entity;

import com.bigtree.chef.orders.model.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;


import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

	private UUID chefId;
	private String customerEmail;
	private String customerMobile;
	private String reference;
	private String currency;
	private String serviceMode;
	private String paymentReference;
	private BigDecimal deliveryFee;
	private BigDecimal packagingFee;
	private BigDecimal saleTax;
	private BigDecimal subTotal;
	private BigDecimal total;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@JsonFormat(pattern="dd/MMM/yyyy")
	private LocalDate expectedDeliveryDate;
	@JsonFormat(pattern="dd/MMM/yyyy")
	private LocalDate expectedPickupDate;
	private LocalDateTime orderCreated;
	private LocalDateTime orderAccepted;
	private LocalDateTime orderCollected;
	private LocalDateTime orderDelivered;
	private LocalDateTime orderPickedUpByDeliveryPartner;
	private boolean deleted;


	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private Chef chef;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private Customer customer;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private List<OrderItem> items;


}
