package com.bigtree.chef.orders.controller;

import java.util.*;

import com.bigtree.chef.orders.entity.Order;
import com.bigtree.chef.orders.model.enums.Action;
import com.bigtree.chef.orders.model.request.UpdateStatus;
import com.bigtree.chef.orders.model.response.ActionResponse;
import com.bigtree.chef.orders.model.response.Orders;
import com.bigtree.chef.orders.repository.OrderRepository;
import com.bigtree.chef.orders.service.OrderService;

import com.bigtree.chef.orders.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class OrderController {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    PaymentService paymentService;

    @CrossOrigin(origins = "*")
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> orders(@RequestParam(required = false) Map<String, String> qparams) {
        List<Order> orders;
        if (CollectionUtils.isEmpty(qparams)) {
            log.info("Received request to get all orders");
            orders = orderRepository.findAllOrderByDateDesc();
        } else {
            log.info("Received request to get orders with query {}", qparams.toString());
            orders = orderService.findOrdersWithQuery(qparams);
        }
        if (CollectionUtils.isEmpty(orders)) {
            orders = new ArrayList<>();
        }
        log.info("Returning {} orders", orders.size());
        return ResponseEntity.ok().body(orders);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/orders/search")
    public ResponseEntity<Orders> findAllMatch(@RequestParam(required = false) Map<String, String> qparams) {
        List<Order> orders = null;
        if (CollectionUtils.isEmpty(qparams)) {
            log.info("Received request to get all orders");
            orders = orderRepository.findAllOrderByDateDesc();
        } else {
            log.info("Received request to get orders with query {}", qparams.toString());
            orders = orderService.findOrdersWithQuery(qparams);
        }
        List<Order> orderList = new ArrayList<>();
        if (orders != null) {
            orders.forEach(orderList::add);
        }
        return ResponseEntity.ok().body(Orders.builder().orders(orders).build());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getById(@PathVariable("id") UUID id) {
        log.info("Received request to get order by id {}", id);
        Optional<Order> order = orderRepository.findById(id);
        return ResponseEntity.ok().body(order.isPresent() ? order.get() : null);
    }

    @DeleteMapping("/orders/{id}")
    public ResponseEntity<ActionResponse> delete(@PathVariable("id") UUID id) {
        log.info("Received request to delete order by id {}", id);
        boolean deleted = orderService.delete(id);
        return ResponseEntity.ok()
                .body(ActionResponse.builder().action(Action.DELETE).status(deleted).object("Order").id(id).build());
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/orders/{id}")
    public ResponseEntity<ActionResponse> updateStatus(@RequestBody UpdateStatus status,
            @PathVariable("id") UUID id) {
        log.info("Received request to update order by id {}", id);
        boolean updated = orderService.updateStatus(id, status);
        return ResponseEntity.ok()
                .body(ActionResponse.builder().action(Action.UPDATE).status(updated).object("Order").id(id).build());
    }



    @CrossOrigin(origins = "*")
    @PutMapping("/orders/{orderId}/items/{itemId}/cancellation")
    public ResponseEntity<ActionResponse> requestItemCancellation(@PathVariable("orderId") UUID orderId, @PathVariable("itemId") UUID itemId) {
        log.info("Received request to cancel an item {} in the order {}", itemId, orderId);
        boolean success = orderService.cancelItem(itemId);
        return ResponseEntity.ok()
                .body(ActionResponse.builder().action(Action.UPDATE).status(success).object("Order").id(orderId).build());
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/orders/{orderId}/cancellation")
    public ResponseEntity<ActionResponse> requestOrderCancellation(@PathVariable("orderId") UUID orderId) {
        log.info("Received request to cancel an order {}", orderId);
        boolean success = orderService.cancelOrder(orderId);
        return ResponseEntity.ok()
                .body(ActionResponse.builder().action(Action.UPDATE)
                        .requestDescription("Requested to cancel an order")
                        .responseDescription(success ? "Request successful.": "Request failed")
                        .status(success).object("Order").id(orderId).build());
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/orders")
    public ResponseEntity<Order> create(@RequestBody Order order) {
        log.info("Received request to create new order. {}", order.toString());
        Order created = orderService.create(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
