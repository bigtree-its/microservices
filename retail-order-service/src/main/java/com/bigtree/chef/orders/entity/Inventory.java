package com.bigtree.chef.orders.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Table(name = "inventory")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventory extends BaseEntity {

    private UUID productId;
    private UUID purchaseOrderId;
    @JsonFormat(pattern="dd/MMM/yyyy")
    private Date purchaseOrderDate;
    private String productName;
    private String image;
    private String unit;
    private Integer inStock;
    private Integer inReserve;
    private Integer inOrder;
}
