package com.bigtree.customer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {

//    @JsonInclude(JsonInclude.Include.NON_NULL)
    String endpoint;
    String message;
    
}
