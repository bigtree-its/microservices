package com.bigtree.customer.error;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ApiError {
    String reference;
    String title;
    String detail;
    Integer status;

}
