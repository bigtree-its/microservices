package com.bigtree.chef.orders.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.bigtree.chef.orders.error.ApiException;
import com.bigtree.chef.orders.model.enums.OrderStatus;
import com.bigtree.chef.orders.model.request.UpdateStatus;
import com.bigtree.chef.orders.repository.OrderRepository;
import com.bigtree.chef.orders.entity.Order;
import com.bigtree.chef.orders.entity.OrderItem;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;

@Service
@Slf4j
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    EmailService customerEmailService;

    @Autowired
    PaymentService paymentService;


    @Transactional
    public Order create(Order order) {
        if (CollectionUtils.isEmpty(order.getItems())){
            log.error("No items found in ths order");
            throw new ApiException(HttpStatus.BAD_REQUEST, "No items found in ths order");
        }
        final List<OrderItem> items = new ArrayList<>(order.getItems());
        log.info("Saving order with {} items", items.size());
        Long countByDate = orderRepository.countByOrderCreated(LocalDateTime.now());
        order.setReference(buildOrderReference(countByDate.intValue()));
        order.setStatus(OrderStatus.CREATED);
        String serviceMode = order.getServiceMode();
        if ( StringUtils.isEmpty(serviceMode)){
            serviceMode = "PICKUP";
        }else{
            serviceMode = serviceMode.toUpperCase();
        }
        order.setServiceMode(serviceMode);
        if (order.getOrderCreated() == null) {
            order.setOrderCreated(LocalDateTime.now());
        }
        if (order.getCurrency() == null) {
            order.setCurrency("GBP");
        }
        try{
            Order saved = orderRepository.save(order);
            if (saved != null) {
                log.info("Order created: {}", saved.getId());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendOrderConfirmation(order);
                }
            }).start();
            new Thread(() -> paymentService.updatePaymentForOrder(saved)).start();
                return saved;
            } else {
                log.error("Order not created for customer {}", order.getCustomerEmail());
            }
        } catch (Exception e){
            log.error("Error while saving order: {}", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private String buildOrderReference(int count) {
        count = count + 1;
        final String date = LocalDate.now().toString();
        if (count < 10) {
            return date + "000" + count;
        } else if (count < 100) {
            return date + "00" + count;
        } else if (count < 1000) {
            return date + "0" + count;
        } else {
            return date + "" + count;
        }
    }

    private void sendOrderConfirmation(Order order) {
        String subject = "Your BigChef order #" + order.getReference();
        Map<String, Object> body = new HashMap<>();
        body.put("order", order);
        body.put("customer", order.getCustomer());
        body.put("items", order.getItems());
        body.put("chef", order.getChef());
        customerEmailService.sendMail(order.getCustomerEmail(), subject, "order", body);
    }

    public boolean updateStatus(UUID id, UpdateStatus status) {
        Optional<Order> findById = orderRepository.findById(id);
        if (findById.isPresent()) {
            Order order = findById.get();
            order.setStatus(OrderStatus.valueOf(status.getStatus()));
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    public boolean delete(UUID id) {
        Optional<Order> findById = orderRepository.findById(id);
        if (findById.isPresent()) {
            Order order = findById.get();
            orderRepository.delete(order);
            return true;
        }
        return false;
    }

    public List<Order> findOrdersWithQuery(Map<String, String> qParams) {
        final List<Order> result = new ArrayList<>();
        qParams.forEach((k, v) -> {
            if (k.equalsIgnoreCase("customerEmail")) {
                log.info("Looking for orders with customerEmail {}", v);
                result.addAll(orderRepository.findByCustomerEmail(v));
            } else if (k.equalsIgnoreCase("reference")) {
                log.info("Looking for orders with reference {}", v);
                result.addAll(orderRepository.findByReference(v));
            } else if (k.equalsIgnoreCase("status")) {
                log.info("Looking for orders with status {}", v);
                result.addAll(orderRepository.findByStatus(OrderStatus.valueOf(v)));
            } else if (k.equalsIgnoreCase("deleted")) {
                log.info("Looking for deleted orders");
                result.addAll(orderRepository.findByDeleted(true));
            }

        });
        return result;
    }

}
