package com.bigtree.chef.orders.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chef {
    @JsonProperty("chefId")
    String chefId;
    @JsonProperty("name")
    String name;
    @JsonProperty("image")
    String image;
    @JsonProperty("specials")
    String[] specials;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address address;

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Contact contact;
}
