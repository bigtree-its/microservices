package com.bigtree.fapi.service;

import com.bigtree.fapi.entity.Account;
import com.bigtree.fapi.entity.Transaction;
import com.bigtree.fapi.error.ApiException;
import com.bigtree.fapi.helper.ApiConstants;
import com.bigtree.fapi.helper.CsvHelper;
import com.bigtree.fapi.model.ITransactionCsv;
import com.bigtree.fapi.model.TransactionQuery;
import com.bigtree.fapi.model.TransactionResponse;
import com.bigtree.fapi.model.TransactionSummary;
import com.bigtree.fapi.repository.TransactionSpecRepository;
import com.bigtree.fapi.repository.TransactionSpecification;
import com.bigtree.fapi.repository.TransactionsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class TransactionsService {

    @Autowired
    TransactionsRepository repository;

    @Autowired
    TransactionSpecRepository transactionSpecRepository;

    @Autowired
    private TransactionSpecification transactionSpecification;

    @Autowired
    CsvHelper csvHelper;

    @Autowired
    TransactionSpecification transactionDAO;

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
        toSave.setAccountId(account.getId());
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
                valid = Arrays.asList("DR", "CR", "BP", "CHG", "DD", "TFR").contains(toSave.getCode());
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

    public TransactionResponse getAllTransactions(TransactionQuery request) {
        log.info("Fetching transactions {}", request);
        List<Transaction> list = null;
        Page<Transaction> pages = null;
        if (request.getPageNumber() == null) {
            pages = new PageImpl<>(transactionSpecRepository.findAll(transactionSpecification.findTransactions(request)));
        } else {
            if (request.getPageSize() == null) {
                request.setPageSize(10);
            }
            Pageable paging = PageRequest.of(request.getPageNumber() - 1, request.getPageSize());
            pages = transactionSpecRepository.findAll(transactionSpecification.findTransactions(request), paging);
        }
        if (pages != null && pages.getContent() != null) {
            list = pages.getContent();
            if (!CollectionUtils.isEmpty(list)) {
                TransactionResponse transactionResponse = TransactionResponse.builder()
                        .totalPages(pages.getTotalPages())
                        .totalCount(pages.getTotalElements())
                        .pageNo(pages.getNumber() + 1)
                        .transactions(pages.getContent())
                        .build();
                LocalDate oldest = null;
                LocalDate newest = null;
                for (Transaction transaction : list) {
                    if (oldest == null) {
                        oldest = transaction.getDate();
                    } else {
                        if (transaction.getDate().isBefore(oldest)) {
                            oldest = transaction.getDate();
                        }
                    }
                    if (newest == null) {
                        newest = transaction.getDate();
                    } else {
                        if (transaction.getDate().isAfter(newest)) {
                            newest = transaction.getDate();
                        }
                    }
                    if (transaction.getType().equalsIgnoreCase(ApiConstants.TransactionType.Credit)) {
                        transactionResponse.addCredit(transaction.getCategory() == null ? transaction.getDescription() : transaction.getCategory(), transaction.getAmount());
                    } else if (transaction.getType().equalsIgnoreCase(ApiConstants.TransactionType.DEBIT)) {
                        transactionResponse.addDebit(transaction.getCategory() == null ? transaction.getDescription() : transaction.getCategory(), transaction.getAmount());
                    }
                }
                transactionResponse.setNewestDate(newest);
                transactionResponse.setOldestDate(oldest);
                return transactionResponse;
            }
        }
        return null;
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

    public List<Transaction> getAllTransactions(UUID accountId, String transactionType) {
        if (transactionType.equalsIgnoreCase(ApiConstants.TransactionType.DEBIT)) {
            return getAllDebitTransactions(accountId);
        }
        return getAllCredits(accountId);
    }

    public void update(Transaction request, Transaction exist) {
        if (request.getAmount() != null) {
            exist.setAmount(request.getAmount());
        }
        if (request.getCategory() != null) {
            exist.setCategory(request.getCategory());
        }
        if (request.getType() != null) {
            exist.setType(request.getType());
        }
        if (request.getCode() != null) {
            exist.setCode(request.getCode());
        }
        if (request.getBalance() != null) {
            exist.setBalance(request.getBalance());
        }
        if (request.getDate() != null) {
            exist.setDate(request.getDate());
        }
        repository.save(exist);
    }
}
