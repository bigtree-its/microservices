package com.bigtree.user.repository;

import com.bigtree.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT a from User a where a.email = :email")
    User findByEmail(@Param("email") String email);
}
