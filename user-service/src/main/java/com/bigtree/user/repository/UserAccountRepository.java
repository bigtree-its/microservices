package com.bigtree.user.repository;

import com.bigtree.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {

    @Query("SELECT a from UserAccount a where a.userId = :userId")
    UserAccount getAccountByUserId(@Param("userId") UUID userId);
}
