package com.bigtree.ecomm.orders.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Table(name = "refunds")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Refund extends BaseEntity {

    private UUID orderId;
    @JsonFormat(pattern="dd/MMM/yyyy")
    private LocalDate date;
    private BigDecimal amount;
    private String currency;
    private String paymentMethod;
    private String customerEmail;
    private String status;
}
