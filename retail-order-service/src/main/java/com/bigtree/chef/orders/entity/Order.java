package com.bigtree.chef.orders.entity;

import com.bigtree.chef.orders.model.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.Enumerated;
import javax.persistence.EnumType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@ToString
@Table(name = "orders")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends BaseEntity {

	@JsonFormat(pattern="dd/MMM/yyyy")
	private LocalDate date;

	@Singular
	@Fetch(FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order", fetch = FetchType.EAGER)
	private Set<OrderItem> items;
	private String customerEmail;
	private String reference;
	private String currency;
	private String paymentReference;
	private BigDecimal shippingCost;
	private BigDecimal saleTax;
	private BigDecimal subTotal;
	private BigDecimal totalCost;
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Column(columnDefinition="BOOLEAN DEFAULT false", nullable = false)
	private boolean cancellationRequested;

	@Column(columnDefinition="BOOLEAN DEFAULT false", nullable = false)
	private boolean cancellationApproved;

	@Column(columnDefinition="BOOLEAN DEFAULT false", nullable = false)
	private boolean cancellationDeclined;

	@Column(columnDefinition="BOOLEAN DEFAULT false", nullable = false)
	private boolean cancelled;

	private boolean deleted;

	@Type(type = "jsonb")
	@Column(columnDefinition = "jsonb")
	private Address address;

	@JsonFormat(pattern="dd/MMM/yyyy")
	private LocalDate expectedDeliveryDate;

}
