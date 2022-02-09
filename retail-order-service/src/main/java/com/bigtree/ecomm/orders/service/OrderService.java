package com.bigtree.ecomm.orders.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import com.bigtree.ecomm.orders.entity.Order;
import com.bigtree.ecomm.orders.entity.OrderItem;
import com.bigtree.ecomm.orders.model.enums.OrderStatus;
import com.bigtree.ecomm.orders.model.request.UpdateStatus;
import com.bigtree.ecomm.orders.repository.ItemRepository;
import com.bigtree.ecomm.orders.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;

@Service
@Slf4j
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    BasketService basketService;

    @Transactional
    public String create(Order order) {
        Set<OrderItem> items = new HashSet<>(order.getItems());
        log.info("Saving order with {} items", items.size());
        for (OrderItem orderItem : items) {
            orderItem.setOrder(order);
        }
        Long countByDate = orderRepository.countByDate(LocalDate.now());
        order.setReference(buildOrderReference(countByDate.intValue()));
        order.setStatus(OrderStatus.CREATED);
        if (order.getDate() == null) {
            order.setDate(LocalDate.now());
        }
        if (order.getCurrency() == null) {
            order.setCurrency("GBP");
        }
        Order saved = orderRepository.save(order);
        if (saved != null) {
            log.info("Order {} created for user {}", saved.getReference(), saved.getEmail());
            for (OrderItem orderItem : items) {
                orderItem.setOrder(saved);
            }
            itemRepository.saveAll(items);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendOrderConfirmation(order);
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    basketService.delete(saved.getEmail());
                }
            }).start();

            return saved.getReference();
        } else {
            log.error("Order not created for user {}", order.getEmail());
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
        String subject = "Your BigTree order #" + order.getReference();
        Map<String, Object> body = new HashMap<>();
        body.put("order", order);
        body.put("items", order.getItems());
        body.put("address", order.getAddress());
        emailService.sendMail(order.getEmail(), subject, "order", body);
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
            order.setDateDeleted(LocalDateTime.now());
            orderRepository.save(order);
            return true;
        }
        return false;
    }

    public List<Order> findOrdersWithQuery(Map<String, String> qParams) {
        final List<Order> result = new ArrayList<>();
        qParams.forEach((k, v) -> {
            if (k.equalsIgnoreCase("email")) {
                log.info("Looking for orders with email {}", v);
                result.addAll(orderRepository.findByEmailOrderByDateDesc(v));
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

    public boolean cancelItem(UUID itemId) {
        log.info("Requesting cancellation of an item {}", itemId);
        Optional<OrderItem> orderItem = itemRepository.findById(itemId);
        if (orderItem != null && orderItem.isPresent()) {
            OrderItem item = orderItem.get();
            item.setCancellationRequested(true);
            log.info("Request Cancellation success. item {}", itemId);
            itemRepository.save(item);
            return true;
        }
        log.info("Item {} not found. Cannot request cancellation", itemId);
        return false;
    }

    public boolean cancelOrder(UUID orderId) {
        log.info("Requesting cancellation of an order {}", orderId);
        Optional<Order> order = orderRepository.findById(orderId);
        if (order != null && order.isPresent()) {
            Order item = order.get();
            if (item.getStatus() == OrderStatus.CREATED || item.getStatus() == OrderStatus.PROCESSING) {
                item.setCancellationRequested(true);
                log.info("Request Cancellation success. Order {}", orderId);
                orderRepository.save(item);
                return true;
            } else {
                log.info("Cannot Request Cancellation. Order already {}", item.getStatus());
                return false;
            }
        }
        log.info("Item {} not found. Cannot request cancellation", orderId);
        return false;
    }
}
