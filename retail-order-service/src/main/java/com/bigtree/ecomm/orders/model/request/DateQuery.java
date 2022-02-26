package com.bigtree.ecomm.orders.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DateQuery {
    private String column;
    private LocalDate date;
    private LocalDate startDate;
    private LocalDate enDate;
}
