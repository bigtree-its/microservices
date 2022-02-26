package com.bigtree.ecomm.orders.controller;

import com.bigtree.ecomm.orders.entity.Payment;
import com.bigtree.ecomm.orders.model.enums.Action;
import com.bigtree.ecomm.orders.model.response.ActionResponse;
import com.bigtree.ecomm.orders.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@Slf4j
public class PaymentController {

    @Autowired
    PaymentService service;

    @CrossOrigin(origins = "*")
    @PostMapping("/payments")
    public ResponseEntity<Payment> create(@RequestBody Payment payment){
        log.info("Received request to create payment");
        Payment created = service.create(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/payments/{id}")
    public ResponseEntity update(@PathVariable("id") UUID id, @RequestBody Payment payment){
        log.info("Received request to update payment by id {}", id);
        service.update(id, payment);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/payments/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Payment> findOne(@PathVariable("id") UUID id) {
        log.info("Received request to get payment by id {}", id);
        Payment one = service.findOne(id);
        return ResponseEntity.ok().body(one);
    }

    @GetMapping("/payments")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<Payment>> findAll(){
        log.info("Received request to get all payments");
        List<Payment> all = service.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(all);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/payments/search")
    public ResponseEntity<List<Payment>> findAllMatch(@RequestParam(required = false) Map<String, Object> qparams) {
        List<Payment> result = null;
        if (CollectionUtils.isEmpty(qparams)) {
            log.info("Received request to get all payments");
            result = service.findAll();
        } else {
            log.info("Received request to get payments with query {}", qparams.toString());
            result = service.findBy(qparams);
        }
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/payments/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<ActionResponse> deleteOne(@PathVariable("id") UUID id) {
        log.info("Received request to delete payment by id {}", id);
        boolean deleted = service.deleteOne(id);
        return ResponseEntity.ok()
                .body(ActionResponse.builder()
                        .action(Action.DELETE)
                        .status(deleted)
                        .object("Payment")
                        .id(id)
                        .reference(UUID.randomUUID())
                        .build());
    }

    @DeleteMapping("/payments")
    @CrossOrigin(origins = "*")
    public ResponseEntity deleteAll(){
        log.info("Received request to delete all payments");
        service.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
