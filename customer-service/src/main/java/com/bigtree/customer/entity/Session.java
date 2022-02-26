package com.bigtree.customer.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@ToString
@Table(name = "sessions")
@NoArgsConstructor
public class Session {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID customerId;
    private UUID sessionId;
    private LocalDateTime start;
    private LocalDateTime finish;
}
