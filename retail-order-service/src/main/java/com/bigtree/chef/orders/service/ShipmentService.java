package com.bigtree.chef.orders.service;

import com.bigtree.chef.orders.entity.Shipment;
import com.bigtree.chef.orders.error.ApiException;
import com.bigtree.chef.orders.model.request.DateQuery;
import com.bigtree.chef.orders.model.request.ShipmentSearchQuery;
import com.bigtree.chef.orders.repository.ShipmentRepository;
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
public class ShipmentService {

    // Columns
    private static final String COLUMN_COLLECTED_DATE = "CollectedDate";
    private static final String COLUMN_ESTIMATED_COLLECTION_DATE = "EstimatedCollectionDate";
    private static final String COLUMN_DELIVERED_DATE = "DeliveredDate";
    private static final String COLUMN_ESTIMATED_DELIVERY_DATE = "EstimatedDeliveryDate";
    private static final String COLUMN_COURIER_PICKUP_DATE_TIME = "CourierPickupDateTime";

    // Collection
    private static final String QUERY_BY_ESTIMATED_COLLECTION_DATE = "EstimatedCollectionDate";
    private static final String QUERY_BY_ESTIMATED_COLLECTION_DATE_BETWEEN = "EstimatedCollectionDateBetween";
    private static final String QUERY_BY_COLLECTED_DATE = "CollectedDate";
    private static final String QUERY_BY_COLLECTED_DATE_BETWEEN = "CollectedDateBetween";

    // Delivery
    private static final String QUERY_BY_ESTIMATED_DELIVERY_DATE = "EstimatedDeliveryDate";
    private static final String QUERY_BY_ESTIMATED_DELIVERY_DATE_BETWEEN = "EstimatedDeliveryDateBetween";
    private static final String QUERY_BY_DELIVERED_DATE = "DeliveredDate";
    private static final String QUERY_BY_DELIVERED_DATE_BETWEEN = "DeliveredDateBetween";

    private static final String QUERY_BY_COURIER_PICKUP_DATE_TIME = "CourierPickupDateTime";

    @Autowired
    ShipmentRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    public Shipment create(Shipment shipment) {
        log.info("Create shipment object");
        Shipment saved = repository.save(shipment);
        if (saved != null) {
            log.info("Created new shipment {}", saved.getId());
        }
        return saved;
    }

    public Shipment update(UUID id, Shipment shipment) {
        log.info("Updating Shipment {}", id);
        Shipment one = this.findOne(id);
        if (one == null) {
            log.info("Cannot update. No shipment found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot update. No shipment found");
        }
        if (shipment.getStatus() != null) {
            one.setStatus(shipment.getStatus());
        }
        if (shipment.getEstimatedCollectionDate() != null) {
            one.setEstimatedCollectionDate(shipment.getEstimatedCollectionDate());
        }
        if (shipment.getCollectedDate() != null) {
            one.setCollectedDate(shipment.getCollectedDate());
        }
        if (shipment.getEstimatedDeliveryDate() != null) {
            one.setEstimatedDeliveryDate(shipment.getEstimatedDeliveryDate());
        }
        if (shipment.getDeliveredDate() != null) {
            one.setDeliveredDate(shipment.getDeliveredDate());
        }
        if (shipment.getCourierPickupDateTime() != null) {
            one.setCourierPickupDateTime(shipment.getCourierPickupDateTime());
        }
        if (shipment.getCourierCharge() != null) {
            one.setCourierCharge(shipment.getCourierCharge());
        }
        if (shipment.getVatCharge() != null) {
            one.setVatCharge(shipment.getVatCharge());
        }
        if (shipment.getVatPercentage() != null) {
            one.setVatPercentage(shipment.getVatPercentage());
        }
        if (shipment.getPackageCharge() != null) {
            one.setPackageCharge(shipment.getPackageCharge());
        }
        Shipment updated = repository.save(one);
        if (updated != null) {
            log.info("Updated Shipment {}", id);
        }
        return updated;
    }

    public Shipment findOne(UUID id) {
        log.info("Finding Shipment for {}", id);
        Optional<Shipment> optionalShipment = repository.findById(id);
        if (optionalShipment.isPresent()) {
            return optionalShipment.get();
        }
        log.info("No shipment found for {}", id);
        return null;
    }

    public List<Shipment> findAll() {
        log.info("Finding all Shipments");
        List<Shipment> all = repository.findAll();
        log.info("Found {} shipments", all.size());
        return all;
    }

    public List<Shipment> findByQuery(ShipmentSearchQuery query) {
        log.info("Finding Shipments for the query");
        if (query == null) {
            log.info("No shipments found");
            return Collections.EMPTY_LIST;
        }
        List<Shipment> result = Collections.EMPTY_LIST;
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

        log.info("Found {} Shipments", result.size());
        return result;

    }

    private NamedQueries getNamedQueries(DateQuery dateQuery) {
        NamedQueries namedQueries = null;
        switch (dateQuery.getColumn()) {
            case COLUMN_COLLECTED_DATE:
                namedQueries = NamedQueries.builder().betweenDateQuery(QUERY_BY_COLLECTED_DATE_BETWEEN).singleDateQuery(QUERY_BY_COLLECTED_DATE).build();
                break;
            case COLUMN_DELIVERED_DATE:
                namedQueries = NamedQueries.builder().betweenDateQuery(QUERY_BY_DELIVERED_DATE_BETWEEN).singleDateQuery(QUERY_BY_DELIVERED_DATE).build();
                break;
            case COLUMN_ESTIMATED_COLLECTION_DATE:
                namedQueries = NamedQueries.builder().betweenDateQuery(QUERY_BY_ESTIMATED_COLLECTION_DATE_BETWEEN).singleDateQuery(QUERY_BY_ESTIMATED_COLLECTION_DATE).build();
                break;
            case COLUMN_ESTIMATED_DELIVERY_DATE:
                namedQueries = NamedQueries.builder().betweenDateQuery(QUERY_BY_ESTIMATED_DELIVERY_DATE_BETWEEN).singleDateQuery(QUERY_BY_ESTIMATED_DELIVERY_DATE).build();
                break;
            case COLUMN_COURIER_PICKUP_DATE_TIME:
                namedQueries = NamedQueries.builder().betweenDateQuery(QUERY_BY_COURIER_PICKUP_DATE_TIME).singleDateQuery(QUERY_BY_COURIER_PICKUP_DATE_TIME).build();
                break;
        }
        return namedQueries;
    }

    private List<Shipment> runDateQuery(DateQuery dateQuery, NamedQueries namedQueries) {
        if (dateQuery.getStartDate() != null && dateQuery.getEnDate() != null) {
            Query q = entityManager.createNamedQuery(namedQueries.betweenDateQuery);
            q.setParameter("startDate", dateQuery.getStartDate());
            q.setParameter("endDate", dateQuery.getEnDate());
            List<Shipment> shipments = (List<Shipment>) q.getResultList();
            if (CollectionUtils.isEmpty(shipments)) {
                shipments = Collections.EMPTY_LIST;
            }
            log.info("{} shipments found", shipments.size());
            return shipments;
        } else if (dateQuery.getDate() != null) {
            Query q = entityManager.createNamedQuery(namedQueries.singleDateQuery);
            q.setParameter(1, dateQuery.getDate());
            List<Shipment> shipments = (List<Shipment>) q.getResultList();
            if (CollectionUtils.isEmpty(shipments)) {
                shipments = Collections.EMPTY_LIST;
            }
            log.info("{} shipments found", shipments.size());
            return shipments;
        }
        return Collections.EMPTY_LIST;
    }

    public boolean deleteOne(UUID id) {
        log.info("Deleting shipment {}", id);
        Shipment one = this.findOne(id);
        if (one != null) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    public void deleteAll() {
        log.info("Deleting all shipments");
        repository.deleteAll();
    }

    @Data
    @Builder
    private static class NamedQueries {
        String singleDateQuery;
        String betweenDateQuery;
    }
}

