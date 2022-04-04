package com.bigtree.chef.orders.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Contact {

    @JsonProperty("telephone")
    String telephone;
    @JsonProperty("mobile")
    String mobile;
    @JsonProperty("email")
    String email;
}
