package com.bigtree.ecomm.orders.service;

import com.bigtree.ecomm.orders.entity.Basket;
import com.bigtree.ecomm.orders.entity.Inventory;
import com.bigtree.ecomm.orders.error.ApiException;
import com.bigtree.ecomm.orders.model.request.UpdateInventoryRequest;
import com.bigtree.ecomm.orders.model.response.InventoryItem;
import com.bigtree.ecomm.orders.model.response.InventoryResponse;
import com.bigtree.ecomm.orders.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;

    public InventoryResponse check(final Basket basket) {
        log.info("Checking inventory for basket {}", StringUtils.isEmpty(basket.getEmail()) ? basket.getIp(): basket.getEmail());
        final List<InventoryItem> inventoryItems = new ArrayList<>();
        final InventoryResponse response = InventoryResponse.builder()
                .items(inventoryItems)
                .build();
        if (basket != null && !CollectionUtils.isEmpty(basket.getItems())) {
            basket.getItems().forEach(i -> {
                final Inventory inventory = inventoryRepository.findByProductId(i.getProductId());
                if (inventory != null && inventory.getInStock() > i.getQuantity()) {
                    log.error("Found Inventory record for product {} : InStock: {}, InReserve: {}", i.getProductId(), inventory.getInStock(), inventory.getInReserve());
                    final InventoryItem inventoryItem = InventoryItem.builder()
                            .inStock(inventory.getInStock())
                            .requested(i.getQuantity())
                            .productId(i.getProductId())
                            .inReserve(inventory.getInReserve())
                            .build();
                    inventoryItems.add(inventoryItem);
                }else{
                    log.error("No inventory record available for product {}", i.getProductId());
                    final InventoryItem inventoryItem = InventoryItem.builder()
                            .inStock(0)
                            .inReserve(0)
                            .requested(i.getQuantity())
                            .productId(i.getProductId())
                            .build();
                    inventoryItems.add(inventoryItem);
                }
            });
        }
        return response;
    }

    public void update(UUID productId, Inventory request) {
        log.info("Updating inventory for product {}", productId);
        Inventory inventory = inventoryRepository.findByProductId(productId);
        if (inventory != null) {
            Integer inStock = inventory.getInStock();
            if (request.getInStock() != null) {
                inStock = inStock + request.getInStock();
            }
            Integer inReserve = inventory.getInReserve();
            if (request.getInReserve() != null) {
                inReserve = inReserve + request.getInReserve();
            }
            Integer inOrder = inventory.getInOrder();
            if (request.getInOrder() != null) {
                inOrder = inOrder + request.getInOrder();
            }
            inventory.setInStock(inStock);
            inventory.setInReserve(inReserve);
            inventory.setInOrder(inOrder);
        }else{
            if (StringUtils.isEmpty(request.getProductName())){
                log.error("Product name is mandatory");
                throw new ApiException( HttpStatus.BAD_REQUEST,"Product name is mandatory");
            }
            inventory = Inventory.builder()
                    .productId(request.getProductId())
                    .productName(request.getProductName())
                    .inStock(request.getInStock())
                    .inReserve(request.getInReserve())
                    .inOrder(request.getInOrder())
                    .purchaseOrderId(request.getPurchaseOrderId())
                    .build();
        }
        inventoryRepository.save(inventory);
        log.info("Inventory updated for product {}", request.getProductId());
    }

    public List<Inventory> listAll() {
        log.info("Retrieving all inventory");
        List<Inventory> objects = new ArrayList<>();
        Iterable<Inventory> all = inventoryRepository.findAll();
        for (Inventory inventory : all) {
            objects.add(inventory);
        }
        return objects;

    }

    public Inventory get(UUID productId) {
        log.info("Retrieving inventory for productId: {}", productId);
        return inventoryRepository.findByProductId(productId);
    }
}
