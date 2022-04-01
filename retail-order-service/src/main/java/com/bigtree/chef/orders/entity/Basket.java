package com.bigtree.chef.orders.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Table(name = "baskets")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Basket extends BaseEntity {

	@JsonFormat(pattern="dd/MMM/yyyy")
	private LocalDate date;

	@Singular
	@Fetch(FetchMode.SUBSELECT)
	@OneToMany(cascade = CascadeType.REMOVE, mappedBy = "basket", fetch = FetchType.EAGER)
	private Set<BasketItem> items;
	private String customerEmail;
	private String ip;
	private BigDecimal total;

	public void dismissChild(BasketItem child) {
		this.items.remove(child);
	}

//	@PreRemove
//	public void dismissChildren() {
//		this.items.forEach(child -> child.dismissParent()); // SYNCHRONIZING THE OTHER SIDE OF RELATIONSHIP
//		this.items.clear();
//	}
}
