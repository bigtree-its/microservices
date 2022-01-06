package com.bigtree.fapi.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

@Data
@Entity
@ToString
@Builder
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID accountId;
    @JsonFormat(pattern="dd/MMM/yyyy")
    private LocalDate date;
    private String description;
    private String type;
    private String code;
    private String category;
    private BigDecimal amount;
    private BigDecimal balance;
}
