package com.bigtree.ecomm.orders.entity;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.bigtree.ecomm.orders.entity.BaseEntity;

import com.bigtree.ecomm.orders.entity.Basket;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "basket_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BasketItem extends BaseEntity {

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    @JoinColumn(name = "basket_id", nullable = false )
    private Basket basket;
    
    @Column(nullable = false)
    @NotEmpty
    private UUID productId;

    @Column(nullable = false)
    @NotEmpty
    private String image;

    @Column(nullable = false)
    @NotEmpty
    private String productName;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

//    @PreRemove
//    public void dismissParent(){
//        basket.dismissChild(this);
//        basket = null;
//    }

}
