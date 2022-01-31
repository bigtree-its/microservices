package com.bigtree.user.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegistrationRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String password;

}
