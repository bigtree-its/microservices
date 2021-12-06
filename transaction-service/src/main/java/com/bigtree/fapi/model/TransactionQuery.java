package com.bigtree.fapi.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class TransactionQuery {

    UUID accountId;
    String type;
    String code;
    LocalDate oldestDate;
    LocalDate newestDate;
    BigDecimal minimumAmount;
    BigDecimal maximumAmount;
    Integer pageNumber;
    Integer pageSize;

}
