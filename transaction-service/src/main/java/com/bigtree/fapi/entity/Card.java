package com.bigtree.fapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Card {

    @JsonProperty("number")
    String number;
    @JsonProperty("nameOnCard")
    String nameOnCard;
    @JsonProperty("expiry")
    String expiry;
    @JsonProperty("cvv")
    String cvv;
    @JsonProperty("type")
    String type;
    @JsonProperty("company")
    String company;
    @JsonProperty("pin")
    String pin;
}
