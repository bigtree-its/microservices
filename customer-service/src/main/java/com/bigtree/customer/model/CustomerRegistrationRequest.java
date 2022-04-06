package com.bigtree.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRegistrationRequest {

    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private String mobile;
    private String password;
}
