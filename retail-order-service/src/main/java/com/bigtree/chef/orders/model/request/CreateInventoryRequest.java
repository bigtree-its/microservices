package com.bigtree.chef.orders.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateInventoryRequest {

    private String productId;
    private Integer available;
}
