package com.bigtree.chef.orders.controller;

import com.bigtree.chef.orders.entity.Delivery;
import com.bigtree.chef.orders.model.enums.Action;
import com.bigtree.chef.orders.model.request.DeliverySearchQuery;
import com.bigtree.chef.orders.model.response.ActionResponse;
import com.bigtree.chef.orders.service.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class DeliveryController {

    @Autowired
    DeliveryService service;

    @CrossOrigin(origins = "*")
    @PostMapping("/deliveries")
    public ResponseEntity<Delivery> create(@RequestBody Delivery delivery){
        log.info("Received request to create delivery");
        Delivery created = service.create(delivery);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/deliveries/{id}")
    public ResponseEntity update(@PathVariable("id") UUID id, @RequestBody Delivery delivery){
        log.info("Received request to update delivery by id {}", id);
        service.update(id, delivery);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


    @GetMapping("/deliveries/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<Delivery> findOne(@PathVariable("id") UUID id) {
        log.info("Received request to get delivery by id {}", id);
        Delivery one = service.findOne(id);
        return ResponseEntity.ok().body(one);
    }

    @GetMapping("/deliveries")
    @CrossOrigin(origins = "*")
    public ResponseEntity<List<Delivery>> findAll(){
        log.info("Received request to get all deliveries");
        List<Delivery> all = service.findAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(all);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/deliveries/search")
    public ResponseEntity<List<Delivery>> findByQuery(@RequestBody DeliverySearchQuery query) {
        List<Delivery> result = null;
        if (query == null) {
            log.info("Received request to get all deliveries");
            result = service.findAll();
        } else {
            log.info("Received request to get deliveries with query {}", query.toString());
            result = service.findByQuery(query);
        }
        return ResponseEntity.ok().body(result);
    }

    @DeleteMapping("/deliveries/{id}")
    @CrossOrigin(origins = "*")
    public ResponseEntity<ActionResponse> deleteOne(@PathVariable("id") UUID id) {
        log.info("Received request to delete delivery by id {}", id);
        boolean deleted = service.deleteOne(id);
        return ResponseEntity.ok()
                .body(ActionResponse.builder()
                        .action(Action.DELETE)
                        .status(deleted)
                        .object("Delivery")
                        .id(id)
                        .reference(UUID.randomUUID())
                        .build());
    }

    @DeleteMapping("/deliveries")
    @CrossOrigin(origins = "*")
    public ResponseEntity deleteAll(){
        log.info("Received request to delete all deliveries");
        service.deleteAll();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
