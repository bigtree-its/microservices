package com.bigtree.chef.orders.service;

import com.bigtree.chef.orders.repository.PaymentRepository;
import com.bigtree.chef.orders.entity.Order;
import com.bigtree.chef.orders.entity.Payment;
import com.bigtree.chef.orders.error.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class PaymentService {

    @Autowired
    PaymentRepository repository;

    public Payment create(Payment payment) {
        log.info("Create payment object");
        Payment saved = repository.save(payment);
        if ( saved != null){
            log.info("Created new payment {}", saved.getId());
        }
        return saved;
    }

    public Payment update(UUID id, Payment payment) {
        log.info("Updating Payment {}", id);
        Payment one = this.findOne(id);
        if ( one == null){
            log.info("Cannot update. No payment found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot update. No payment found");
        }
        if ( payment.getPaymentMethod() != null){
            one.setPaymentMethod(payment.getPaymentMethod());
        }
        if ( payment.getAmount() != null){
            one.setAmount(payment.getAmount());
        }
        if ( payment.getCustomerEmail() != null){
            one.setCustomerEmail(payment.getCustomerEmail());
        }
        if ( payment.getCurrency() != null){
            one.setCurrency(payment.getCurrency());
        }
        if ( payment.getStatus() != null){
            one.setStatus(payment.getStatus());
        }
        if ( payment.getStripeReference() != null){
            one.setStripeReference(payment.getStripeReference());
        }
        Payment updated = repository.save(one);
        if ( updated != null){
            log.info("Updated Payment {}", id);
        }
        return updated;
    }

    public Payment findOne(UUID id) {
        log.info("Finding Payment for {}", id);
        Optional<Payment> optionalPayment = repository.findById(id);
        if ( optionalPayment.isPresent()){
            return optionalPayment.get();
        }
        log.info("No payment found for {}", id);
        return null;
    }

    public List<Payment> findAll() {
        log.info("Finding all Payments");
        List<Payment> all = repository.findAll();
        log.info("Found {} payments", all.size());
        return all;
    }

    public List<Payment> findBy(Map<String, Object> queryParams) {
        log.info("Finding Payment for the query");
        if (CollectionUtils.isEmpty(queryParams)) {
            log.info("No payments found");
            return Collections.EMPTY_LIST;
        }
        LocalDate startDate = null;
        LocalDate endDate = null;
        List<Payment> result = Collections.EMPTY_LIST;
        for (Map.Entry<String, Object> e : queryParams.entrySet()) {
            if (e.getKey().equalsIgnoreCase("orderId")) {
                result.add(repository.findByOrderId((UUID) e.getValue()));
            }
            if (e.getKey().equalsIgnoreCase("customerEmail")) {
                result = repository.findByCustomerEmail((String) e.getValue());
            }
            if (e.getKey().equalsIgnoreCase("stripeReference")) {
                result.add(repository.findByStripeReference((String) e.getValue()));
            }
            if (e.getKey().equalsIgnoreCase("date")) {
                result = repository.findByDate((LocalDate) e.getValue());
            }
            if (e.getKey().equalsIgnoreCase("startDate")) {
                startDate = (LocalDate) e.getValue();
            }
            if (e.getKey().equalsIgnoreCase("endDate")) {
                endDate = (LocalDate) e.getValue();
            }
            if (e.getKey().equalsIgnoreCase("status")) {
                result = repository.findByStatus((String) e.getValue());
            }
        }

        if (CollectionUtils.isEmpty(result)) {
            if (startDate != null && endDate != null) {
                result = repository.findByDates(startDate, endDate);
            } else if (startDate != null || endDate != null) {
                log.error("Rate Limiting Error. Due to volume of data, startDate and endDate must be given");
                throw new ApiException(HttpStatus.BAD_REQUEST, "Rate Limiting Error. Due to volume of data, startDate and endDate must be given");
            }
        }
        log.info("Found {} Payments", result.size());
        return result;

    }

    public boolean deleteOne(UUID id) {
        log.info("Deleting payment {}", id);
        Payment one = this.findOne(id);
        if ( one != null){
            repository.deleteById(id);
            return true;
        }else{
            return false;
        }
    }

    public void deleteAll() {
        log.info("Deleting all payments");
        repository.deleteAll();
    }

    public Payment findByReference(String paymentReference) {
        log.info("Finding Payment for Payment Reference {}", paymentReference);
        final Payment byStripeReference = repository.findByStripeReference(paymentReference);
        if ( byStripeReference != null){
            return byStripeReference;
        }
        log.info("No payment found for reference {}", paymentReference);
        return null;
    }

    public void updatePaymentForOrder(Order order){
        log.info("Updating payment for the customer {} order {}", order.getCustomerEmail(), order.getId() );
        final Payment byReference = findByReference(order.getPaymentReference());
        if ( byReference != null){
            byReference.setStatus("succeeded");
            byReference.setOrderId(order.getId());
            byReference.setCustomerEmail(order.getCustomerEmail());
            repository.save(byReference);
        }
    }
}
