package com.bigtree.fapi.controller;

import com.bigtree.fapi.entity.Account;
import com.bigtree.fapi.model.ApiResponse;

import com.bigtree.fapi.service.AccountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class AccountsController {

    @Autowired
    AccountsService accountsService;

    @GetMapping("/get")
    public ResponseEntity<ApiResponse> get(){
        log.info("Received request to get response");
        return ResponseEntity.ok().body(ApiResponse.builder().endpoint("/get").method(HttpMethod.GET.name()).build());
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAllAccounts(){
        log.info("Received request to get all accounts");
        List<Account> accounts = accountsService.getAllAccounts();
        return ResponseEntity.ok().body(accounts);
    }
    @GetMapping(value = "/accounts/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable UUID accountId){
        log.info("Received request to get account {}", accountId);
        Account account = accountsService.getAccount(accountId);
        return ResponseEntity.ok().body(account);
    }

    @PostMapping(value = "/accounts", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> createAccount(@Valid @RequestBody Account account){
        log.info("Received request to create new account {}", account);
        Account newAccount = accountsService.saveAccount(account);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    @PutMapping(value = "/accounts/{accountId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> updateAccount(@PathVariable UUID accountId, @Valid @RequestBody Account account){
        log.info("Received request to update account {}", accountId);
        Account updated = accountsService.updateAccount(accountId, account);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping(value = "/accounts/{accountId}")
    public ResponseEntity<Void> updateAccount(@PathVariable UUID accountId){
        log.info("Received request to delete account {}", accountId);
        accountsService.deleteAccount(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
