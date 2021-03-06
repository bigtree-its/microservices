package com.bigtree.chef.orders.repository;

import java.util.List;
import java.util.UUID;

import com.bigtree.chef.orders.entity.Basket;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Repository class for <code>Basket</code> domain objects All method names are compliant
 * with Spring Data naming conventions sb this interface can easily be extended for Spring
 * Data. See:
 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#repositories.query-methods.query-creation
 *
 * @author Nav
 */
@Repository
public interface BasketRepository extends JpaRepository<Basket, UUID> {
    
    /**
	 * Retrieve all <code>Basket</code>s from the data store for given customer's email
	 * @return a <code>Collection</code> of <code>Basket</code>s
	 */
    @Transactional(readOnly = true)
    Basket findByCustomerEmail(@Param("customerEmail") String customerEmail);

	/**
	 * Retrieve all <code>Basket</code>s from the data store for given IP
	 * @return a <code>Collection</code> of <code>Basket</code>s
	 */
	@Transactional(readOnly = true)
	@Query("SELECT b from Basket b where b.ip = :ip")
	Basket findByIP(@Param("ip")  String ip);

    /**
	 * Retrieve all <code>Basket</code>s from the data store.
	 * @return a <code>Collection</code> of <code>Basket</code>s
	 */
    @Transactional(readOnly = true)
    @Query("SELECT b FROM Basket b")
	List<Basket> findAll() throws DataAccessException;

}
