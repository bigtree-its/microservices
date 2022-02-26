package com.bigtree.ecomm.orders.controller;

import com.bigtree.ecomm.orders.entity.Shipment;
import com.bigtree.ecomm.orders.model.enums.Action;
import com.bigtree.ecomm.orders.model.request.ShipmentSearchQuery;
import com.bigtree.ecomm.orders.model.response.ActionResponse;
import com.bigtree.ecomm.orders.service.ShipmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class ShipmentsController {

    @Autowired
    ShipmentService service;

    @CrossOrigin(origins = "*")
    @PostMapping("/shipments")
    public ResponseEntity<Shipment> create(@RequestBody Shipment payment){
        log.info("Received request to create payment");
        Shipment created = service.create(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/shipments/{id}")
    public ResponseEntity update(@PathVariable("id") UUID id, @RequestBody Shipment payment){
        log.info("Received request to update payment by id {}", id);
        service.update(id, payment);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/shipments/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Shipment> findOne(@PathVariable("id") UUID id) {
        log.info("Received request to get payment by id {}", id);
        Shipment one = service.findOne(id);
        return ResponseEntity.ok().body(one);
    }

    @GetMapping("/shipments")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<Shipment>> findAll(){
        log.info("Received request to get all payments");
        List<Shipment> all = service.findAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(all);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/shipments/search")
    public ResponseEntity<List<Shipment>> findByQuery(@RequestBody ShipmentSearchQuery query) {
        List<Shipment> result = null;
        if (query == null) {
            log.info("Received request to get all payments");
            result = service.findAll();
        } else {
            log.info("Received request to get payments with query {}", query.toString());
            result = service.findByQuery(query);
        }
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/shipments/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<ActionResponse> deleteOne(@PathVariable("id") UUID id) {
        log.info("Received request to delete payment by id {}", id);
        boolean deleted = service.deleteOne(id);
        return ResponseEntity.ok()
                .body(ActionResponse.builder()
                        .action(Action.DELETE)
                        .status(deleted)
                        .object("Shipment")
                        .id(id)
                        .reference(UUID.randomUUID())
                        .build());
    }

    @DeleteMapping("/shipments")
    @CrossOrigin(origins = "*")
    public ResponseEntity deleteAll(){
        log.info("Received request to delete all payments");
        service.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
