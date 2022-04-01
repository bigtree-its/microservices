package com.bigtree.chef.orders.service;

import com.bigtree.chef.orders.entity.Delivery;
import com.bigtree.chef.orders.model.request.DateQuery;
import com.bigtree.chef.orders.model.request.DeliverySearchQuery;
import com.bigtree.chef.orders.repository.DeliveryRepository;
import com.bigtree.chef.orders.error.ApiException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;

@Service
@Slf4j
public class DeliveryService {

    private static final String COLUMN_DELIVERED_DATE = "DeliveredDate";
    private static final String COLUMN_ESTIMATED_DELIVERY_DATE = "EstimatedDeliveryDate";

    // Delivery
    private static final String QUERY_BY_ESTIMATED_DELIVERY_DATE = "EstimatedDeliveryDate";
    private static final String QUERY_BY_ESTIMATED_DELIVERY_DATE_BETWEEN = "EstimatedDeliveryDateBetween";
    private static final String QUERY_BY_DELIVERED_DATE = "DeliveredDate";
    private static final String QUERY_BY_DELIVERED_DATE_BETWEEN = "DeliveredDateBetween";

    private static final String QUERY_BY_ORDER_PICKED_UP_BY_DELIVERY_PARTNER_DATE_TIME = "OrderPickedUpByDeliveryPartnerDateTime";
    private static final String QUERY_BY_ORDER_PICKED_UP_BY_DELIVERY_PARTNER_DATE_TIME_BETWEEN = "OrderPickedUpByDeliveryPartnerDateTimeBetween";

    @Autowired
    DeliveryRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public Delivery create(Delivery delivery) {
        log.info("Create delivery object");
        Delivery saved = repository.save(delivery);
        if (saved != null) {
            log.info("Created new delivery {}", saved.getId());
        }
        return saved;
    }

    public Delivery update(UUID id, Delivery delivery) {
        log.info("Updating Delivery {}", id);
        Delivery one = this.findOne(id);
        if (one == null) {
            log.info("Cannot update. No delivery found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot update. No delivery found");
        }
        if (delivery.getStatus() != null) {
            one.setStatus(delivery.getStatus());
        }
        if (delivery.getEstimatedDeliveryDate() != null) {
            one.setEstimatedDeliveryDate(delivery.getEstimatedDeliveryDate());
        }
        if (delivery.getDeliveredDate() != null) {
            one.setDeliveredDate(delivery.getDeliveredDate());
        }
        if (delivery.getOrderPickedUpByDeliveryPartner() != null) {
            one.setOrderPickedUpByDeliveryPartner(delivery.getOrderPickedUpByDeliveryPartner());
        }
        if (delivery.getDeliveryPartnerCharge() != null) {
            one.setDeliveryPartnerCharge(delivery.getDeliveryPartnerCharge());
        }
        if (delivery.getVatCharge() != null) {
            one.setVatCharge(delivery.getVatCharge());
        }
        if (delivery.getVatPercentage() != null) {
            one.setVatPercentage(delivery.getVatPercentage());
        }
        if (delivery.getPackageCharge() != null) {
            one.setPackageCharge(delivery.getPackageCharge());
        }
        Delivery updated = repository.save(one);
        if (updated != null) {
            log.info("Updated Delivery {}", id);
        }
        return updated;
    }

    public Delivery findOne(UUID id) {
        log.info("Finding Delivery for {}", id);
        Optional<Delivery> optionalShipment = repository.findById(id);
        if (optionalShipment.isPresent()) {
            return optionalShipment.get();
        }
        log.info("No shipment found for {}", id);
        return null;
    }

    public List<Delivery> findAll() {
        log.info("Finding all Deliveries");
        List<Delivery> all = repository.findAll();
        log.info("Found {} deliveries", all.size());
        return all;
    }

    public List<Delivery> findByQuery(DeliverySearchQuery query) {
        log.info("Finding Deliveries for the query");
        if (query == null) {
            log.info("No deliveries found");
            return Collections.EMPTY_LIST;
        }
        List<Delivery> result = Collections.EMPTY_LIST;
        final List<DateQuery> dateQueries = query.getDateQueries();
        if (!CollectionUtils.isEmpty(dateQueries)) {
            for (DateQuery dateQuery : dateQueries) {
                if (!StringUtils.isEmpty(dateQuery.getColumn())) {
                    return runDateQuery(dateQuery, getNamedQueries(dateQuery));
                }
            }
        }
        if (query.getCustomerEmail() != null) {
            result = repository.findByCustomerEmail(query.getCustomerEmail());
        }
        if (query.getOrderId() != null) {
            result = repository.findByOrderId(query.getOrderId());
        }
        if (query.getTrackingReference() != null) {
            result.add(repository.findByTrackingReference(query.getTrackingReference()));
        }
        if (query.getStatus() != null) {
            result.addAll(repository.findByStatus(query.getStatus().name()));
        }

        log.info("Found {} Deliveries", result.size());
        return result;

    }

    private NamedQueries getNamedQueries(DateQuery dateQuery) {
        NamedQueries namedQueries = null;
        switch (dateQuery.getColumn()) {
            case QUERY_BY_ORDER_PICKED_UP_BY_DELIVERY_PARTNER_DATE_TIME:
                namedQueries = NamedQueries.builder().betweenDateQuery(QUERY_BY_ORDER_PICKED_UP_BY_DELIVERY_PARTNER_DATE_TIME_BETWEEN).singleDateQuery(QUERY_BY_ORDER_PICKED_UP_BY_DELIVERY_PARTNER_DATE_TIME).build();
                break;
            case COLUMN_DELIVERED_DATE:
                namedQueries = NamedQueries.builder().betweenDateQuery(QUERY_BY_DELIVERED_DATE_BETWEEN).singleDateQuery(QUERY_BY_DELIVERED_DATE).build();
                break;
            case COLUMN_ESTIMATED_DELIVERY_DATE:
                namedQueries = NamedQueries.builder().betweenDateQuery(QUERY_BY_ESTIMATED_DELIVERY_DATE_BETWEEN).singleDateQuery(QUERY_BY_ESTIMATED_DELIVERY_DATE).build();
                break;
        }
        return namedQueries;
    }

    private List<Delivery> runDateQuery(DateQuery dateQuery, NamedQueries namedQueries) {
        if (dateQuery.getStartDate() != null && dateQuery.getEnDate() != null) {
            Query q = entityManager.createNamedQuery(namedQueries.betweenDateQuery);
            q.setParameter("startDate", dateQuery.getStartDate());
            q.setParameter("endDate", dateQuery.getEnDate());
            List<Delivery> deliveries = (List<Delivery>) q.getResultList();
            if (CollectionUtils.isEmpty(deliveries)) {
                deliveries = Collections.EMPTY_LIST;
            }
            log.info("{} deliveries found", deliveries.size());
            return deliveries;
        } else if (dateQuery.getDate() != null) {
            Query q = entityManager.createNamedQuery(namedQueries.singleDateQuery);
            q.setParameter(1, dateQuery.getDate());
            List<Delivery> deliveries = (List<Delivery>) q.getResultList();
            if (CollectionUtils.isEmpty(deliveries)) {
                deliveries = Collections.EMPTY_LIST;
            }
            log.info("{} deliveries found", deliveries.size());
            return deliveries;
        }
        return Collections.EMPTY_LIST;
    }

    public boolean deleteOne(UUID id) {
        log.info("Deleting shipment {}", id);
        Delivery one = this.findOne(id);
        if (one != null) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public void deleteAll() {
        log.info("Deleting all deliveries");
        repository.deleteAll();
    }

    @Data
    @Builder
    private static class NamedQueries {
        String singleDateQuery;
        String betweenDateQuery;
    }
}

