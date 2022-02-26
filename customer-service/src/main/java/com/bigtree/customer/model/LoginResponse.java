package com.bigtree.customer.model;

import com.bigtree.customer.entity.Customer;
import com.bigtree.customer.entity.Session;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private Session session;
    private Customer customer;
    private Boolean success;
    private String message;
}
