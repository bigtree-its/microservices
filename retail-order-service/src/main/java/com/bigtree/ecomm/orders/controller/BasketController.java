package com.bigtree.ecomm.orders.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.bigtree.ecomm.orders.entity.Basket;
import com.bigtree.ecomm.orders.model.enums.Action;
import com.bigtree.ecomm.orders.model.response.ActionResponse;
import com.bigtree.ecomm.orders.repository.BasketRepository;
import com.bigtree.ecomm.orders.service.BasketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class BasketController {

    @Autowired
    BasketRepository basketRepository;

    @Autowired
    BasketService basketService;

    @CrossOrigin(origins = "*")
    @GetMapping("/baskets")
    public ResponseEntity<List<Basket>> baskets(@RequestParam(required = false) Map<String, String> qParams) {
        List<Basket> baskets = null;
        if (CollectionUtils.isEmpty(qParams)) {
            log.info("Received request to get all baskets");
            baskets = basketRepository.findAll();
        } else {
            log.info("Received request to get baskets with query {}", qParams.toString());
            baskets = basketService.findBaskets(qParams);
        }
        List<Basket> basketList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(baskets)) {
            baskets.forEach(basketList::add);
        }
        log.info("Returning {} baskets ", basketList.size());
        return ResponseEntity.ok().body(basketList);
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/baskets")
    public ResponseEntity<ActionResponse> updateBasket(@RequestBody Basket basket, @RequestParam("basketId") UUID basketId, @RequestParam("createIfNew") boolean createIfNew) {
        log.info("Received request to update basket {}", basketId);
        boolean updated = basketService.updateBasket(basketId, basket, createIfNew);
        return ResponseEntity.ok().body(ActionResponse.builder().action(Action.UPDATE).status(updated).object("Basket").build());
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/baskets")
    public ResponseEntity<ActionResponse> delete(@RequestParam("basketId") UUID basketId) {
        log.info("Received request to delete basket by id {}", basketId);
        boolean deleted = basketService.deleteById(basketId);
        return ResponseEntity.ok()
                .body(ActionResponse.builder().action(Action.DELETE).status(deleted).object("basket").id(basketId).build());
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/baskets")
    public ResponseEntity<Void> create(@RequestBody Basket basket) {
        log.info("Received request to create new basket. {}", basket);
        basketService.create(basket);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
