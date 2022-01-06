package com.bigtree.fapi.controller;

import com.bigtree.fapi.entity.Account;
import com.bigtree.fapi.entity.Transaction;
import com.bigtree.fapi.error.ApiException;
import com.bigtree.fapi.helper.ApiConstants;
import com.bigtree.fapi.model.ApiResponse;
import com.bigtree.fapi.model.TransactionQuery;
import com.bigtree.fapi.model.TransactionResponse;
import com.bigtree.fapi.service.AccountsService;
import com.bigtree.fapi.service.TransactionsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class TransactionsController {

    @Autowired
    TransactionsService transactionsService;

    @Autowired
    AccountsService accountsService;

    @GetMapping("/transactions")
    public ResponseEntity<TransactionResponse> getTransactionsForAccount(
            @RequestHeader("accountId") UUID accountId,
            @RequestHeader(name = "X-Transaction-Type", required = false) String type,
            @RequestHeader(name = "X-Transaction-Code", required = false) String code,
            @RequestHeader(name = "X-Oldest-Date", required = false)  @DateTimeFormat(pattern = ApiConstants.FAPI_DATE_FORMAT) LocalDate oldestDate,
            @RequestHeader(name = "X-Newest-Date", required = false) @DateTimeFormat(pattern = ApiConstants.FAPI_DATE_FORMAT) LocalDate newestDate,
            @RequestHeader(name = "X-Minimum-Amount", required = false) BigDecimal minimumAmount,
            @RequestHeader(name = "X-Maximum-Amount", required = false) BigDecimal maximumAmount,
            @RequestHeader(name = "X-Page-Number", required = false) Integer pageNumber,
            @RequestHeader(name = "X-Page-Size", required = false) Integer pageSize){
        log.info("Received request to get transactions for account {}", accountId);
        verifyAccount(accountId);
        List<Transaction> transactions;
        TransactionQuery transactionQuery = TransactionQuery.builder()
                .accountId(accountId)
                .code(code)
                .type(type)
                .oldestDate(oldestDate)
                .newestDate(newestDate)
                .minimumAmount(minimumAmount)
                .maximumAmount(maximumAmount)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();
        TransactionResponse response = transactionsService.getAllTransactions(transactionQuery);
        return ResponseEntity.ok().body(response);
    }

    private Account verifyAccount(UUID accountId) {
        if ( accountId == null){
            throw new ApiException(HttpStatus.BAD_REQUEST, "AccountId Header is Mandatory");
        }
        Account account = accountsService.getAccount(accountId);
        if ( account == null){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Account not found "+ accountId);
        }
        return account;
    }

    @GetMapping(value = "/transactions/{transactionId}")
    public ResponseEntity<Transaction> getAccount(@PathVariable UUID transactionId){
        log.info("Received request to get single transaction {}", transactionId);
        Transaction transaction = transactionsService.getTransaction(transactionId);
        return ResponseEntity.ok().body(transaction);
    }

    @PostMapping(value = "/transactions", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Transaction> createSingleTransaction(@Valid @RequestBody Transaction transaction, @RequestHeader("accountId") UUID accountId){
        log.info("Received request to create new transaction {}", transaction);
        Account account = verifyAccount(accountId);
        final Transaction saved = transactionsService.save(transaction, account);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PutMapping(value = "/transactions/{transactionId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Void> updateTransaction(@Valid @RequestBody Transaction transaction, @PathVariable("transactionId") UUID transactionId){
        log.info("Received request to create new transaction {}", transactionId);
        Transaction exist = transactionsService.getTransaction(transactionId);
        if ( exist == null){
            throw new ApiException(HttpStatus.BAD_REQUEST, "Transaction not found "+ transactionId);
        }
        transactionsService.update(transaction, exist);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(value = "/transactions")
    public ResponseEntity<ApiResponse> uploadBulkTransactions(@RequestParam("file") MultipartFile file, @RequestHeader("accountId") UUID accountId){
        log.info("Received request to upload bulk transaction for account {}", accountId);
        Account account = verifyAccount(accountId);
        int count = transactionsService.uploadBulk(file, account);
        return new ResponseEntity<>(ApiResponse.builder().endpoint("POST /transactions").message(count+ " Transactions Created.").build(), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/transactions/{transactionId}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable UUID transactionId){
        log.info("Received request to delete transaction {}", transactionId);
        transactionsService.deleteSingleTransaction(transactionId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/transactions")
    public ResponseEntity<Void> deleteTransactionByAccount(@RequestHeader("accountId") UUID accountId){
        log.info("Received request to delete all transactions for account {}", accountId);
        verifyAccount(accountId);
        transactionsService.deleteTransactions(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
