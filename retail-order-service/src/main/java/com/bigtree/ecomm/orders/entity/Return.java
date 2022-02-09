package com.bigtree.ecomm.orders.entity;

import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.bigtree.ecomm.orders.model.enums.ReturnStatus;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "returns")
@Entity
@EqualsAndHashCode(callSuper=false)
public class Return extends BaseEntity {
    
    private UUID orderId;

    @Singular
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "returns", fetch = FetchType.EAGER)
    private Set<ReturnItem> returnItems;
    
    @Enumerated(EnumType.ORDINAL)
    private ReturnStatus status;
}