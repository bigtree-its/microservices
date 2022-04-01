package com.bigtree.chef.orders.model.request;

import com.bigtree.chef.orders.model.enums.DeliveryStatus;
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
public class DeliverySearchQuery {
    private List<DateQuery> dateQueries;
    private UUID orderId;
    private UUID trackingReference;
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;
    private String customerEmail;

}


