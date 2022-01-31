package com.bigtree.merchant.entity;

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
@Table(name = "merchants")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Merchant {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String displayName;
    private String number;
    private String vatNumber;
    private String type;
    private String status;
    private String postcode;
    private String latitude;
    private String longitude;
    private String aboutUs;
    private String logoImg;
    private String thumbnailImg;
    private String customerImages;
    private String cuisines;
    private Integer minimumOrder;
    private Integer collectionEstimate;
    private Integer deliveryEstimateMin;
    private Integer deliveryEstimateMax;
    private Boolean noMinimumOrder;
    private Boolean active;
    private Boolean delivery;
    private Boolean collection;
    private Boolean selfDelivery;
    private String natureOfBusiness;
    @JsonFormat(pattern="dd/MMM/yyyy")
    private LocalDate incorporatedOn;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Address address;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Person person;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private OpeningHours openingHours;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private FoodHygiene foodHygiene;

}
