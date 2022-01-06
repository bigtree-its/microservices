package com.bigtree.fapi.controller;

import com.bigtree.fapi.entity.Account;
import com.bigtree.fapi.entity.Company;
import com.bigtree.fapi.error.ApiException;
import com.bigtree.fapi.helper.ApiConstants;
import com.bigtree.fapi.model.ApiResponse;

import com.bigtree.fapi.service.AccountsService;
import com.bigtree.fapi.service.CompanyService;
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
    @Autowired
    CompanyService companyService;

    @GetMapping("/accounts/business/{companyId}")
    public ResponseEntity<List<Account>> getBusinessAccounts(@PathVariable(name = "companyId") UUID companyId){
        log.info("Received request to get accounts of company {}", companyId);
        List<Account> accounts = accountsService.getBusinessAccounts(companyId);
        return ResponseEntity.ok().body(accounts);
    }

    @GetMapping("/accounts/personal/{userId}")
    public ResponseEntity<List<Account>> getPersonalAccounts(@PathVariable(name = "userId") UUID userId){
        log.info("Received request to get accounts of person {}", userId);
        List<Account> accounts = accountsService.getPersonalAccounts(userId);
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
        verifyAccount(account);
        Account newAccount = accountsService.saveAccount(account);
        return new ResponseEntity<>(newAccount, HttpStatus.CREATED);
    }

    private void verifyAccount(Account account) {
        if ( account.getCompanyId() == null && account.getUserId() == null){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Either Company Id or User Id is mandatory");
        }
        if ( account.getType() == null ){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Account Type is Mandatory");
        }
        if ( account.getType().equalsIgnoreCase(ApiConstants.AccountType.BUSINESS)){
            if ( account.getCompanyId() == null){
                throw new ApiException(HttpStatus.BAD_REQUEST, "Company Id is mandatory for business accounts");
            }else{
                if (companyService.getCompany(account.getCompanyId()) == null){
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Company Id is invalid. No company found");
                }
            }
        }
        if ( account.getType().equalsIgnoreCase(ApiConstants.AccountType.PERSONAL)){
            if ( account.getUserId() == null){
                throw new ApiException(HttpStatus.BAD_REQUEST, "User Id is mandatory for personal accounts");
            }
        }
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
