package com.bigtree.customer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRegistrationRequest {

    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String mobile;
    private String password;

}
