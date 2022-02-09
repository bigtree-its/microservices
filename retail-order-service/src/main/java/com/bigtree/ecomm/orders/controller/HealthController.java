package com.bigtree.ecomm.orders.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<String> health(){
        log.info("Request for /health");
        return ResponseEntity.ok().body("Up and Running");
    }
    
}
