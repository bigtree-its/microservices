package com.bigtree.chef.orders.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.util.UUID;

import javax.persistence.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    @JsonProperty("firstName")
    String firstName;

    @JsonProperty("lastName")
    String lastName;

    @JsonProperty("fullName")
    String fullName;

    @JsonProperty("id")
    UUID id;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address address;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Contact contact;
}
