package com.bigtree.chef.orders.model.request;

import com.bigtree.chef.orders.model.enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipmentSearchQuery {
    private List<DateQuery> dateQueries;
    private UUID orderId;
    private UUID trackingReference;
    @Enumerated(EnumType.STRING)
    private ShipmentStatus status;
    private String customerEmail;

}


