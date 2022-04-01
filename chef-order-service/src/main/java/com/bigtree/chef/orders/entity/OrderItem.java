package com.bigtree.chef.orders.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.*;
import org.hibernate.annotations.Type;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem{
    
//    @JsonIgnore
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "order_id" )
//    private Order order;
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
