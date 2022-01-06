package com.bigtree.fapi.entity;

import java.util.UUID;

import javax.persistence.*;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
@Table(name = "accounts")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID companyId;
    private UUID userId;
    private String type;
    private String subType;
    private String accountName;
    private String sortCode;
    private String accountNumber;
    private String bank;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Card card;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private OnlineAccess onlineAccess;
}
