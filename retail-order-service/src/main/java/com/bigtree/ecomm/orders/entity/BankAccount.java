package com.bigtree.ecomm.orders.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    @JsonProperty("accountNumber")
    String accountNumber;
    @JsonProperty("sortCode")
    String sortCode;
    @JsonProperty("accountName")
    String accountName;
    @JsonProperty("bank")
    String bank;

}
