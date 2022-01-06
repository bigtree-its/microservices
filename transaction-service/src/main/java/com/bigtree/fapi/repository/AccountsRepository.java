package com.bigtree.fapi.repository;

import com.bigtree.fapi.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface AccountsRepository extends JpaRepository<Account, UUID> {

    @Query("SELECT a from Account a where a.id = :accountId")
    Account getAccount(@Param("accountId") UUID accountId);

    @Query("SELECT a from Account a where a.companyId = :companyId")
    List<Account> getBusinessAccounts(@Param("companyId") UUID companyId);

    @Query("SELECT a from Account a where a.userId = :userId")
    List<Account> getPersonalAccounts(@Param("userId") UUID userId);

    @Query("SELECT a from Account a where a.companyId = :companyId")
    Account getAccountByCompanyId(@Param("companyId") UUID companyId);

    @Query("SELECT a from Account a where a.sortCode = :sortCode AND accountNumber = :accountNumber")
    Account findByAccountNumberAndSortCode(@Param("sortCode") String sortCode, @Param("accountNumber") String accountNumber);
}
