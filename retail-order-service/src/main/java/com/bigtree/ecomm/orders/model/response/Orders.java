package com.bigtree.ecomm.orders.model.response;

import java.util.List;

import com.bigtree.ecomm.orders.entity.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orders {
    
    private List<Order> orders;
}
