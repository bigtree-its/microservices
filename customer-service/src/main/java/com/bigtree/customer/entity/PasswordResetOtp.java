package com.bigtree.customer.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@ToString
@Table(name = "resets")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetOtp {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID customerId;
    private UUID otp;
    private LocalDateTime start;
}
