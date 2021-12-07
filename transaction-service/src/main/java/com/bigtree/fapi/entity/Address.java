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
public class Address {

    @JsonProperty("addressLine1")
    String addressLine1;
    @JsonProperty("addressLine2")
    String addressLine2;
    @JsonProperty("city")
    String city;
    @JsonProperty("postcode")
    String postcode;
    @JsonProperty("country")
    String country;
}
