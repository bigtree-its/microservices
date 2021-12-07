package com.bigtree.fapi.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Entity
@ToString
@Table(name = "companies")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Company {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String number;
    private String vatNumber;
    private String type;
    private String status;
    private String natureOfBusiness;
    @JsonFormat(pattern="dd/MMM/yyyy")
    private LocalDate incorporatedOn;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address registeredAddress;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Peoples peoples;


}
