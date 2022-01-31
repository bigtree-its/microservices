package com.bigtree.user.repository;

import com.bigtree.user.entity.Session;
import com.bigtree.user.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface SessionRepository extends JpaRepository<Session, UUID> {

    @Query("SELECT a from Session a where a.userId = :userId")
    Session findByUserId(@Param("userId") UUID userId);
}
