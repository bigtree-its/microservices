package com.bigtree.fapi.model;

import com.bigtree.fapi.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class TransactionResponse {

    Integer totalPages;
    Long totalCount;
    Integer pageNo;
    @JsonFormat(pattern="dd/MMM/yyyy")
    LocalDate oldestDate;
    @JsonFormat(pattern="dd/MMM/yyyy")
    LocalDate newestDate;
    TransactionSummary credits;
    TransactionSummary debits;
    List<Transaction> transactions;

    public void addDebit(String reference, BigDecimal amount) {
        if (debits == null) {
            debits = new TransactionSummary();
        }
        Map<String, BigDecimal> transactions = debits.getTransactions();
        if( transactions == null){
            transactions = new HashMap<>();
        }
        BigDecimal total = debits.getTotal();
        if (transactions.containsKey(reference)) {
            transactions.put(reference, transactions.get(reference).add(amount));
        } else {
            transactions.put(reference, amount);
        }
        total = total.add(amount);
        debits.setTotal(total);
        debits.setTransactions(transactions);
    }
    public void addCredit(String reference, BigDecimal amount) {
        if (credits == null) {
            credits = new TransactionSummary();
        }
        Map<String, BigDecimal> transactions = credits.getTransactions();
        if( transactions == null){
            transactions = new HashMap<>();
        }
        BigDecimal total = credits.getTotal();
        if (transactions.containsKey(reference)) {
            transactions.put(reference, transactions.get(reference).add(amount));
        } else {
            transactions.put(reference, amount);
        }
        total = total.add(amount);
        credits.setTotal(total);
        credits.setTransactions(transactions);
    }

}
