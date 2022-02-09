package com.bigtree.ecomm.orders.model.request;

import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateInventoryRequest {

    private UUID productId;
    private String productName;
    private Integer reserve;
    private Integer add;
    private Integer take;
}
