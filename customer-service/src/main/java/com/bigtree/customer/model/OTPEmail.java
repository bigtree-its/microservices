package com.bigtree.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OTPEmail {

    private String otp;
    private String customerEmail;
    private String customerName;
    private String passwordResetLink;

}
