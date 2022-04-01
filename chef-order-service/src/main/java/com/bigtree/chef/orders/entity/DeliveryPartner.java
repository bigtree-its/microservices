package com.bigtree.chef.orders.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@ToString
@Table(name = "couriers")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryPartner extends BaseEntity {

    private String name;
    private String contactPerson;
    private String vatNumber;
    private String regNumber;
    private String email;
    private String website;
    private String telephone;
    private String mobile;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address billingAddress;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address regAddress;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private BankAccount bankAccount;
}
