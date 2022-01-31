package com.bigtree.user.model;

import com.bigtree.user.entity.Session;
import com.bigtree.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private Session session;
    private User user;
    private Boolean success;
    private String message;
}
