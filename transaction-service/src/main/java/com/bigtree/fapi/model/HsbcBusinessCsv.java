package com.bigtree.fapi.model;

import com.opencsv.bean.CsvBindByName;
import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HsbcBusinessCsv implements ITransactionCsv {

    @CsvBindByName
    String date;
    @CsvBindByName(column = "Type")
    String code;
    @CsvBindByName(column = "Paid In")
    BigDecimal paidIn;
    @CsvBindByName(column = "Paid Out")
    BigDecimal paidOut;
    @CsvBindByName(column = "Balance")
    BigDecimal balance;
    @CsvBindByName
    String description;

    @Override
    public BigDecimal getAmount() {
        return paidIn != null && !paidIn.equals(BigDecimal.ZERO) ? paidIn : paidOut.negate();
    }

    @Override
    public String getType() {
        return paidIn != null && !paidIn.equals(BigDecimal.ZERO) ? "Credit" : "Debit";
    }
}
