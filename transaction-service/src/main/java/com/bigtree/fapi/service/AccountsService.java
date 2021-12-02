package com.bigtree.fapi.service;

import com.bigtree.fapi.entity.Account;
import com.bigtree.fapi.error.ApiException;
import com.bigtree.fapi.repository.AccountsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AccountsService {

    @Autowired
    AccountsRepository repository;

    @Transactional
    public Account saveAccount(Account account){
        log.info("Saving new account");
        Account exist = repository.findByAccountNumberAndSortCode(account.getSortCode(), account.getAccountNumber());
        if ( exist != null && exist.getId() != null){
            log.error("Account already exist");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Account already exist");
        }
        Account save = repository.save(account);
        if ( save != null && save.getId() != null){
            log.info("Account created {}", save.getId());
        }
        return save;
    }

    public List<Account> getAllAccounts(){
        log.info("Fetching all accounts");
        return repository.findAll();
    }

    public Account updateAccount(UUID id, Account account) {
        log.info("Updating account");
        Optional<Account> optional = repository.findById(id);
        if ( optional.isPresent()){
           log.info("Account already exist");
            Account exist = optional.get();
            if (StringUtils.hasLength(account.getAccountName())){
                exist.setAccountName(account.getAccountName());
            }
            if (StringUtils.hasLength(account.getBank())){
                exist.setBank(account.getBank());
            }
            if (StringUtils.hasLength(account.getSecurityNumber())){
                exist.setSecurityNumber(account.getSecurityNumber());
            }
            if (StringUtils.hasLength(account.getSortCode())){
                exist.setSortCode(account.getSortCode());
            }
            if (StringUtils.hasLength(account.getAccountNumber())){
                exist.setAccountNumber(account.getAccountNumber());
            }
            if (StringUtils.hasLength(account.getPin())){
                exist.setPin(account.getPin());
            }
            if (StringUtils.hasLength(account.getCardNumber())){
                exist.setCardNumber(account.getCardNumber());
            }
            if (StringUtils.hasLength(account.getCardType())){
                exist.setCardType(account.getCardType());
            }
            if (StringUtils.hasLength(account.getExpiry())){
                exist.setExpiry(account.getExpiry());
            }
            if (account.getOnlineAccess() != null){
                exist.setOnlineAccess(account.getOnlineAccess());
            }
            Account updated = repository.save(exist);
            if ( updated != null && updated.getId() != null){
                log.info("Account updated {}", updated.getId());
            }
            return updated;
        }else{
            throw new ApiException(HttpStatus.BAD_REQUEST, "Account not exist");
        }

    }

    public void deleteAccount(UUID accountId) {
        Optional<Account> optional = repository.findById(accountId);
        if ( optional.isPresent()) {
            log.info("Account already exist. Deleting account");
            repository.deleteById(accountId);
        }else{
            log.error("Account not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Account not found");
        }
    }

    public Account getAccount(UUID accountId) {
        Optional<Account> optional = repository.findById(accountId);
        if ( optional.isPresent()) {
            log.error("Account found");
            return optional.get();
        }else{
            log.error("Account not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Account not found");
        }
    }
}
