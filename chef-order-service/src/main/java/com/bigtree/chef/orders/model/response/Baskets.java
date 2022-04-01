package com.bigtree.chef.orders.model.response;

import java.util.List;

import com.bigtree.chef.orders.entity.Basket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Baskets {
    
    private List<Basket> baskets;
}
