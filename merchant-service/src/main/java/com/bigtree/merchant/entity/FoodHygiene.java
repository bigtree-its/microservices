package com.bigtree.merchant.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FoodHygiene {

    @JsonProperty("lastInspection")
    @JsonFormat(pattern="dd/MMM/yyyy")
    private LocalDate lastInspection;

}
