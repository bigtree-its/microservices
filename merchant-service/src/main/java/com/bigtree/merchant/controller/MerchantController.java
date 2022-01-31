package com.bigtree.merchant.controller;

import com.bigtree.merchant.entity.Merchant;
import com.bigtree.merchant.service.MerchantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class MerchantController {

    @Autowired
    MerchantService merchantService;

    @GetMapping("/merchants")
    public ResponseEntity<List<Merchant>> getAll(){
        log.info("Received request to get all merchants");
        List<Merchant> merchants = merchantService.getAllMerchants();
        return ResponseEntity.ok().body(merchants);
    }
    
    @GetMapping(value = "/merchants/{merchantId}")
    public ResponseEntity<Merchant> get(@PathVariable UUID merchantId){
        log.info("Received request to get merchant {}", merchantId);
        Merchant merchant = merchantService.getMerchant(merchantId);
        return ResponseEntity.ok().body(merchant);
    }

    @PostMapping(value = "/merchants", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Merchant> create(@Valid @RequestBody Merchant merchant){
        log.info("Received request to create new merchant {}", merchant);
        Merchant newMerchant = merchantService.saveMerchant(merchant);
        return new ResponseEntity<>(newMerchant, HttpStatus.CREATED);
    }

    @PutMapping(value = "/merchants/{merchantId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Merchant> update(@PathVariable UUID merchantId, @Valid @RequestBody Merchant merchant){
        log.info("Received request to update merchant {}", merchantId);
        Merchant updated = merchantService.updateMerchant(merchantId, merchant);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping(value = "/merchants/{merchantId}")
    public ResponseEntity<Void> delete(@PathVariable UUID merchantId){
        log.info("Received request to delete merchant {}", merchantId);
        merchantService.deleteMerchant(merchantId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
