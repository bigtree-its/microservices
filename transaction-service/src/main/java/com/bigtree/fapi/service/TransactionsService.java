package com.bigtree.fapi.service;

import com.bigtree.fapi.entity.Account;
import com.bigtree.fapi.entity.Transaction;
import com.bigtree.fapi.entity.TransactionType;
import com.bigtree.fapi.error.ApiException;
import com.bigtree.fapi.helper.ApiConstants;
import com.bigtree.fapi.helper.CsvHelper;
import com.bigtree.fapi.model.ITransactionCsv;
import com.bigtree.fapi.repository.TransactionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
public class TransactionsService {

    @Autowired
    TransactionsRepository repository;

    @Autowired
    CsvHelper csvHelper;

    public int uploadBulk(MultipartFile file, Account account) {
        List<? extends ITransactionCsv> transactions = csvHelper.parseCsvFile(account, file);
        int count = 0;
        if (!CollectionUtils.isEmpty(transactions)) {
            for (ITransactionCsv iTransaction : transactions) {
                Transaction transaction = csvHelper.toTransaction(iTransaction, account.getId());
                final Transaction created = save(transaction, account);
                if (created != null && created.getId() != null) {
                    count++;
                }
            }
        }
        return count;
    }

    public Transaction save(Transaction toSave, Account account) {
        log.info("Saving new transaction");
        if (toSave != null && !isValidCode(toSave, account)) {
            log.error("Transaction Code is not Valid. {}", toSave.getCode());
            throw new ApiException(HttpStatus.BAD_REQUEST, "Transaction Code: " + toSave.getCode() + " is not Valid");
        }
        if (isDuplicateTransaction(toSave)) {
            log.error("Transaction already exist");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Transaction already exist");
        }
        Transaction saved = repository.save(toSave);
        if (saved != null && saved.getId() != null) {
            log.info("Transaction created {}", saved.getId());
        }
        return saved;
    }

    private boolean isValidCode(Transaction toSave, Account account) {
        boolean valid = false;
        switch (account.getBank()) {
            case ApiConstants.Banks.HSBC:
                valid = Arrays.asList("DR", "CR", "BP", "CHG", "DD").contains(toSave.getCode());
                break;
            case ApiConstants.Banks.SANTANDER:
                valid = Arrays.asList("FAST PAYMENT", "CARD PAYMENT", "CREDIT IN", "INT/CHARGES", "DD").contains(toSave.getCode());
                break;
            default:
        }
        return valid;
    }

    private boolean isDuplicateTransaction(Transaction transaction) {
        List<Transaction> result = repository.findByCodeAndAmountAndDate(transaction.getCode(), transaction.getAmount(), transaction.getDate());
        return !CollectionUtils.isEmpty(result);
    }

    public List<Transaction> getAllDebitTransactions(UUID accountId) {
        log.info("Fetching all Transactions");
        return repository.findAllDebits(accountId);
    }

    public List<Transaction> getAllTransactions(UUID accountId) {
        log.info("Fetching all Transactions");
        return repository.getTransactionsByAccountId(accountId);
    }

    public List<Transaction> getAllCredits(UUID accountId) {
        log.info("Fetching all credit Transactions");
        return repository.findAllCredits(accountId);
    }

    @Transactional
    public void deleteTransactions(UUID accountId) {
        repository.deleteByAccountId(accountId);
    }

    @Transactional
    public void deleteSingleTransaction(UUID transactionId) {
        repository.deleteById(transactionId);
    }

    public Transaction getTransaction(UUID id) {
        Optional<Transaction> optional = repository.findById(id);
        if (optional.isPresent()) {
            log.error("Transaction found");
            return optional.get();
        } else {
            log.error("Transaction not found");
            return null;
        }
    }
}
