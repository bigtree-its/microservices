package com.bigtree.fapi.repository;

import com.bigtree.fapi.entity.Transaction;
import com.bigtree.fapi.model.TransactionQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionSpecification {

    public Specification<Transaction> findTransactions(TransactionQuery request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (!StringUtils.isEmpty(request.getType())) {
                predicates.add(criteriaBuilder.equal(root.get("type"), request.getType()));
            }
            if (!StringUtils.isEmpty(request.getCode())) {
                predicates.add(criteriaBuilder.equal(root.get("code"), request.getCode()));
            }
            if (request.getAccountId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("accountId"), request.getAccountId()));
            }
            if (request.getOldestDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), request.getOldestDate()));
            }
            if (request.getNewestDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), request.getNewestDate()));
            }
            if (request.getMinimumAmount() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), request.getMinimumAmount()));
            }
            if (request.getMaximumAmount() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), request.getMaximumAmount()));
            }
//            if (request.getName() != null && !request.getName().isEmpty()) {
//                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")),
//                        "%" + request.getName().toLowerCase() + "%"));
//            }
            query.orderBy(criteriaBuilder.desc(root.get("date")));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
