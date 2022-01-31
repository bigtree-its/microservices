package com.bigtree.merchant.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address correspondenceAddress;

    @JsonProperty("name")
    String name;
    @JsonProperty("mobile")
    String mobile;
    @JsonProperty("telephone")
    String telephone;
    @JsonProperty("email")
    String email;
    @JsonProperty("role")
    String role;
    @JsonProperty("dob")
    @JsonFormat(pattern="dd/MMM/yyyy")
    LocalDate dob;
    @JsonProperty("appointedOn")
    @JsonFormat(pattern="dd/MMM/yyyy")
    LocalDate appointedOn;
    @JsonProperty("nationality")
    String nationality;
    @JsonProperty("country")
    String country;
}
