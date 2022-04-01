package com.bigtree.chef.orders.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.annotation.PreDestroy;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@MappedSuperclass
@Data
public class BaseEntity{

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@JsonFormat(pattern="dd/MMM/yyyy")
	private LocalDateTime dateCreated;

	@JsonFormat(pattern="dd/MMM/yyyy")
	private LocalDateTime dateUpdated;

	@JsonFormat(pattern="dd/MMM/yyyy")
	private LocalDateTime dateDeleted;

	@PrePersist
    public void prePersist() {
		dateCreated = LocalDateTime.now();
    }
 
    @PreUpdate
    public void preUpdate() {
		dateUpdated = LocalDateTime.now();
	}

	@PreDestroy
	public void preDestroy() {
		dateDeleted = LocalDateTime.now();
	}
	
}
