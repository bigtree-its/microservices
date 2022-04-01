package com.bigtree.chef.orders.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class InventoryItem {

    private UUID productId;
    private Integer requested;
    private Integer inStock;
    private Integer inReserve;
}
