package com.bigtree.customer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OtpValidateResponse {
    @JsonProperty("valid")
    boolean valid;
}
