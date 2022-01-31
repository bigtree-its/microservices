package com.bigtree.user.entity;

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
public class UserAccount {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID userId;
    private String password;
    private LocalDateTime passwordChanged;
}
