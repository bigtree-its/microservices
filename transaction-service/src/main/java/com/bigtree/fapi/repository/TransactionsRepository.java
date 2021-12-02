package com.bigtree.fapi.repository;

import com.bigtree.fapi.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TransactionsRepository extends JpaRepository<Transaction, UUID> {

    @Query("SELECT t from Transaction t where t.id = :id")
    Transaction getTransaction(@Param("id") UUID id);

    @Query("SELECT t from Transaction t where t.accountId = :accountId")
    List<Transaction> getTransactionsByAccountId(@Param("accountId") UUID accountId);

    @Query("SELECT t from Transaction t where t.code = :code AND t.amount = :amount AND t.date = :date")
    List<Transaction> findByCodeAndAmountAndDate(@Param("code") String code, @Param("amount") BigDecimal amount,  @Param("date") LocalDate date);

    @Query("SELECT t from Transaction t where t.accountId = :accountId AND t.code IN (:codes)")
    List<Transaction> getAllTransactionsByCode(@Param("accountId") UUID accountId, @Param("codes") List<String> codes);

    @Query("SELECT t from Transaction t where t.accountId = :accountId AND t.amount > 0")
    List<Transaction> findAllCredits(@Param("accountId") UUID accountId);

    @Query("SELECT t from Transaction t where t.accountId = :accountId AND t.amount < 0")
    List<Transaction> findAllDebits(@Param("accountId") UUID accountId);

    @Modifying
    @Query("DELETE from Transaction t where t.accountId = :accountId")
    void deleteByAccountId(@Param("accountId") UUID accountId);
}
