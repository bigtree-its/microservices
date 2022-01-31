package com.bigtree.merchant.repository;

import com.bigtree.merchant.entity.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MerchantRepository extends JpaRepository<Merchant, UUID> {

    @Query("SELECT a from Merchant a where a.id = :merchantId")
    Merchant getMerchant(@Param("merchantId") UUID merchantId);

    @Query("SELECT a from Merchant a where a.type = :type")
    List<Merchant> findByType(@Param("type") String type);

    @Query("SELECT a from Merchant a where a.number = :number")
    Merchant findByNumber(@Param("number") String number);
}
