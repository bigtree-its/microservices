package com.bigtree.chef.orders.entity;

import java.math.BigDecimal;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "return_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReturnItem extends BaseEntity {

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "return_id", nullable = false )
    private Return returns;
    
    @Column(nullable = false)
    @NotEmpty
    private String productId;

    @Column(nullable = false)
    @NotEmpty
    private String productName;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal total;

}
