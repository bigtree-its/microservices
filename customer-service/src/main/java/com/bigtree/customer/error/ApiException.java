package com.bigtree.customer.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException{

    HttpStatus status;

    public ApiException(HttpStatus status, String message){
        super (message);
        this.status = status;
    }

}
