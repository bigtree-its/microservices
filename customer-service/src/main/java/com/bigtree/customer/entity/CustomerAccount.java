package com.bigtree.customer.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@ToString
@Table(name = "accounts")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAccount {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID customerId;
    private String password;
    private LocalDateTime passwordChanged;
}
