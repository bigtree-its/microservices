package com.bigtree.ecomm.orders.service;

import java.time.LocalDate;
import java.util.*;

import com.bigtree.ecomm.orders.entity.Basket;
import com.bigtree.ecomm.orders.entity.BasketItem;
import com.bigtree.ecomm.orders.repository.BasketItemRepository;
import com.bigtree.ecomm.orders.repository.BasketRepository;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class BasketService {

    @Autowired
    BasketRepository basketRepository;

    @Autowired
    BasketItemRepository basketItemRepository;

    public void create(Basket basket) {
        final Set<BasketItem> items = new HashSet<>(basket.getItems());
        log.info("Creating new basket for user: {}", StringUtils.isEmpty(basket.getEmail()) ? basket.getIp(): basket.getEmail());
        basket.setItems(null);
        if (basket.getDate() == null) {
            basket.setDate(LocalDate.now());
        }
        Basket saved = basketRepository.save(basket);
        if (saved != null) {
            log.info("Basket created for user: {}", StringUtils.isEmpty(saved.getEmail()) ? saved.getIp(): saved.getEmail());
            for (BasketItem basketItem : items) {
                basketItem.setBasket(saved);
            }
            basketItemRepository.saveAll(items);
        } else {
            log.error("Basket not saved for user {}", basket.getEmail());
        }
    }

    public Basket retrieveBasket(String email) {
        log.info("Retrieving a basket for user {}", email);
        return basketRepository.findByEmail(email);
    }

    public boolean deleteById(UUID id) {
        Optional<Basket> byBasketId = basketRepository.findById(id);
        if ( byBasketId.isPresent()){
            basketRepository.delete(byBasketId.get());
            return true;
        }
        return false;
    }

    public List<Basket> findBaskets(Map<String, String> qParams) {
        final List<Basket> result = new ArrayList<>();
        qParams.forEach((k, v) -> {
            if (k.equalsIgnoreCase("email")) {
                log.info("Looking for basket with email {}", v);
                result.add(basketRepository.findByEmail(v));
            } else if (k.equalsIgnoreCase("ip")) {
                log.info("Looking for basket with ip {}", v);
                Optional<Basket> findById = basketRepository.findById(UUID.fromString(v));
                if (findById.isPresent()) {
                    result.add(findById.get());
                }
            } else if (k.equalsIgnoreCase("ip")) {
                log.info("Looking for basket with BasketId {}", v);
                Basket findByIp = basketRepository.findByIP(v);
                if (findByIp != null) {
                    result.add(findByIp);
                }
            }

        });
        return result;
    }

    public boolean updateBasket(UUID id, Basket basket, boolean createIfNew) {
        Optional<Basket> byBasketId = basketRepository.findById(id);
        if (byBasketId.isPresent()) {
            log.info("Found a basket with id {}", id);
            Basket record = byBasketId.get();
            record.setTotal(basket.getTotal());
            Basket updatedBasket = basketRepository.save(record);
            Set<BasketItem> items = basket.getItems();

            List<BasketItem> updateList = new ArrayList<>();
            List<BasketItem> orphans = new ArrayList<>();
            // Update existing items and delete orphan
            for (BasketItem recordItem : updatedBasket.getItems()) {
                boolean found = false;
                for (BasketItem item : items) {
                    if ( item.getProductId() == recordItem.getProductId()){
                        recordItem.setPrice(item.getPrice());
                        recordItem.setQuantity(item.getQuantity());
                        recordItem.setTotal(item.getTotal());
                        recordItem.setProductName(item.getProductName());
                        updateList.add(recordItem);
                        found = true;
                        break;
                    }
                }
                // Delete Orphan
                if (! found){
                    orphans.add(recordItem);
                }
            }

            if (!CollectionUtils.isEmpty(updateList)){
                for (BasketItem item : updateList) {
                    log.info("Updating item {}", item.getProductId());
                    basketItemRepository.save(item);
                }
            }

            if (!CollectionUtils.isEmpty(orphans)){
                for (BasketItem orphan : orphans) {
                    log.info("Deleting orphan {}", orphan.getProductId());
                    basketItemRepository.delete(orphan);
                }
            }

            final Basket saved = basketRepository.save(record);
            // Save new items
            for (BasketItem item : items) {
                boolean found = false;
                for (BasketItem recordItem : saved.getItems()) {
                    if ( item.getProductId() == recordItem.getProductId()){
                        found = true;
                        break;
                    }
                }
                // Delete Orphan
                if (! found){
                    item.setBasket(saved);
                    log.info("Saving new item {} to basket {}", item.getProductId(), StringUtils.isEmpty(saved.getEmail()) ? saved.getIp(): saved.getEmail());
                    basketItemRepository.save(item);
                }
            }
            basketRepository.save(record);
            log.info("Basket is updated");
            return true;
        } else {
            log.info("No basket found with id {}", id);
            if ( createIfNew){
                create(basket);
                return true;
            }
        }
        return false;
    }

    public boolean delete(String email){
        log.info("Cleaning up the user basket {}", email);
        Basket basket = basketRepository.findByEmail(email);
        if ( basket != null){
            basketRepository.deleteById(basket.getId());
            return true;
        }
        return false;
    }

}
