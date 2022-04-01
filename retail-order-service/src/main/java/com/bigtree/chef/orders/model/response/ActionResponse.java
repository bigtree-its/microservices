package com.bigtree.chef.orders.model.response;

import com.bigtree.chef.orders.model.enums.Action;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActionResponse {
    
    private String object;
    private UUID reference;
    private UUID id;
    private Action action;
    private String requestDescription;
    private String responseDescription;
    private String error;
    private boolean status;
}
