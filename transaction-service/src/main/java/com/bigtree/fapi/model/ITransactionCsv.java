package com.bigtree.fapi.model;


import java.math.BigDecimal;

public interface ITransactionCsv {

     BigDecimal getAmount();
     String getCode();
     String getType();
     String getDescription();
     String getDate();
}
