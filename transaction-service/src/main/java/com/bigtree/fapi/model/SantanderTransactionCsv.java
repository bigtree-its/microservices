package com.bigtree.fapi.model;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SantanderTransactionCsv implements ITransactionCsv{

    @CsvBindByName
    String date;
    @CsvBindByName(column = "Type")
    String code;
    @CsvBindByName(column = "Debit/Credit")
    BigDecimal amount;
    @CsvBindByName(column = "Balance")
    BigDecimal balance;
    @CsvBindByName(column = "Merchant/Description")
    String description;

    @Override
    public String getType() {
        return (amount != null && amount.signum() > 0) ? "Credit" : "Debit";
    }
}
