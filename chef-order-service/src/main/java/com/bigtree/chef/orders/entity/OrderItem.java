package com.bigtree.chef.orders.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.Type;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem{
    
    private String image;
    private UUID productId;
    private String name;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subTotal;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private List<Extra> extras;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Extra choice;

}
