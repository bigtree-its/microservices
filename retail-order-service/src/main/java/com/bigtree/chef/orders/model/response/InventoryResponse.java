package com.bigtree.chef.orders.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class InventoryResponse {

    private UUID basketId;
    private List<InventoryItem> items;
}
