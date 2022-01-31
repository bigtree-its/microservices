package com.bigtree.user.repository;

import com.bigtree.user.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, UUID> {

    @Query("SELECT a from PasswordResetOtp a where a.userId = :userId")
    PasswordResetOtp findByUserId(@Param("userId") UUID userId);
}
