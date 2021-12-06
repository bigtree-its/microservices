package com.bigtree.fapi.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class TransactionSummary {
    private BigDecimal total = BigDecimal.ZERO;
    private Map<String, BigDecimal> transactions;
}
