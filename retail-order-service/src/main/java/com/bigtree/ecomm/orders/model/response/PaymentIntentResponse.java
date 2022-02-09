package com.bigtree.ecomm.orders.model.response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class PaymentIntentResponse {
    private String id;
    private String object;
    private Long amount;
    private String clientSecret;
    private String currency;
    private boolean error;
    private boolean liveMode;
    private String errorMessage;
    private String paymentMethod;
    private String status;
    private String chargesUrl;
    private Map<String,String> metaData;
}
