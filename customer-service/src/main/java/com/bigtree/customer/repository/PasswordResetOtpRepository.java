package com.bigtree.customer.repository;

import com.bigtree.customer.entity.PasswordResetOtp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface PasswordResetOtpRepository extends JpaRepository<PasswordResetOtp, UUID> {

    @Query("SELECT a from PasswordResetOtp a where a.customerId = :customerId")
    PasswordResetOtp findByUserId(@Param("customerId") UUID customerId);
}
