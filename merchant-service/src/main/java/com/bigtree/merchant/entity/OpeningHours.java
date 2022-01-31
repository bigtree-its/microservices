package com.bigtree.merchant.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpeningHours {

    @JsonProperty("monday")
    String monday;
    @JsonProperty("tuesday")
    String tuesday;
    @JsonProperty("wednesday")
    String wednesday;
    @JsonProperty("thursday")
    String thursday;
    @JsonProperty("friday")
    String friday;
    @JsonProperty("saturday")
    String saturday;
    @JsonProperty("sunday")
    String sunday;
}
