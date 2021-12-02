package com.bigtree.fapi.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnlineAccess {

    @JsonProperty("username")
    String username;
    @JsonProperty("password")
    String password;
    @JsonProperty("notes")
    String notes;
    @JsonProperty("attributes")
    Map<String,String> attributes;
}
