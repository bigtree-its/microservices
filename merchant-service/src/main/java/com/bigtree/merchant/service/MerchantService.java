package com.bigtree.merchant.service;

import com.bigtree.merchant.entity.Merchant;
import com.bigtree.merchant.error.ApiException;
import com.bigtree.merchant.repository.MerchantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class MerchantService {

    @Autowired
    MerchantRepository repository;

    @Transactional
    public Merchant saveMerchant(Merchant merchant){
        log.info("Saving new merchant");
        Merchant exist = repository.findByNumber(merchant.getNumber());
        if ( exist != null && exist.getId() != null){
            log.error("Merchant already exist");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Merchant already exist");
        }
        Merchant save = repository.save(merchant);
        if ( save != null && save.getId() != null){
            log.info("Merchant created {}", save.getId());
        }
        return save;
    }

    public List<Merchant> getAllMerchants(){
        log.info("Fetching all Merchants");
        return repository.findAll();
    }

    public Merchant updateMerchant(UUID id, Merchant merchant) {
        Optional<Merchant> optional = repository.findById(id);
        if ( optional.isPresent()){
           log.info("Merchant already exist. Updating");
            Merchant exist = optional.get();
            if (StringUtils.hasLength(merchant.getName())){
                exist.setName(merchant.getName());
            }
            if (StringUtils.hasLength(merchant.getDisplayName())){
                exist.setDisplayName(merchant.getDisplayName());
            }
            if (StringUtils.hasLength(merchant.getNumber())){
                exist.setNumber(merchant.getNumber());
            }
            if (StringUtils.hasLength(merchant.getVatNumber())){
                exist.setVatNumber(merchant.getVatNumber());
            }
            if (StringUtils.hasLength(merchant.getType())){
                exist.setType(merchant.getType());
            }
            if (StringUtils.hasLength(merchant.getStatus())){
                exist.setStatus(merchant.getStatus());
            }
            if (StringUtils.hasLength(merchant.getPostcode())){
                exist.setPostcode(merchant.getPostcode());
            }
            if (StringUtils.hasLength(merchant.getLatitude())){
                exist.setLatitude(merchant.getLatitude());
            }
            if (StringUtils.hasLength(merchant.getLongitude())){
                exist.setLongitude(merchant.getLongitude());
            }
            if (StringUtils.hasLength(merchant.getAboutUs())){
                exist.setAboutUs(merchant.getAboutUs());
            }
            if (StringUtils.hasLength(merchant.getLogoImg())){
                exist.setLogoImg(merchant.getLogoImg());
            }
            if (StringUtils.hasLength(merchant.getThumbnailImg())){
                exist.setThumbnailImg(merchant.getThumbnailImg());
            }
            if (StringUtils.hasLength(merchant.getCustomerImages())){
                exist.setCustomerImages(merchant.getCustomerImages());
            }
            if (StringUtils.hasLength(merchant.getCuisines())){
                exist.setCuisines(merchant.getCuisines());
            }
            if (merchant.getMinimumOrder() != null){
                exist.setMinimumOrder(merchant.getMinimumOrder());
            }
            if (merchant.getCollectionEstimate() != null){
                exist.setCollectionEstimate(merchant.getCollectionEstimate());
            }
            if (merchant.getDeliveryEstimateMin() != null){
                exist.setDeliveryEstimateMin(merchant.getDeliveryEstimateMin());
            }
            if (merchant.getDeliveryEstimateMax() != null){
                exist.setDeliveryEstimateMax(merchant.getDeliveryEstimateMax());
            }
            if (merchant.getNoMinimumOrder() != null){
                exist.setNoMinimumOrder(merchant.getNoMinimumOrder());
            }
            if (merchant.getActive() != null){
                exist.setActive(merchant.getActive());
            }
            if (merchant.getDelivery() != null){
                exist.setDelivery(merchant.getDelivery());
            }
            if (merchant.getCollection() != null){
                exist.setCollection(merchant.getCollection());
            }
            if (merchant.getSelfDelivery() != null){
                exist.setSelfDelivery(merchant.getSelfDelivery());
            }
            if (StringUtils.hasLength(merchant.getNatureOfBusiness())){
                exist.setNatureOfBusiness(merchant.getNatureOfBusiness());
            }
            if (merchant.getIncorporatedOn() != null){
                exist.setIncorporatedOn(merchant.getIncorporatedOn());
            }
            if (merchant.getAddress() != null){
                exist.setAddress(merchant.getAddress());
            }
            if (merchant.getPerson() != null){
                exist.setPerson(merchant.getPerson());
            }
            if (merchant.getFoodHygiene() != null){
                exist.setFoodHygiene(merchant.getFoodHygiene());
            }
            if (merchant.getOpeningHours() != null){
                exist.setOpeningHours(merchant.getOpeningHours());
            }
            Merchant updated = repository.save(exist);
            if ( updated != null && updated.getId() != null){
                log.info("Merchant updated {}", updated.getId());
            }
            return updated;
        }else{
            throw new ApiException(HttpStatus.BAD_REQUEST, "Merchant not exist");
        }

    }

    public void deleteMerchant(UUID merchantId) {
        Optional<Merchant> optional = repository.findById(merchantId);
        if ( optional.isPresent()) {
            log.info("Merchant already exist. Deleting merchant");
            repository.deleteById(merchantId);
        }else{
            log.error("Merchant not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Merchant not found");
        }
    }

    public Merchant getMerchant(UUID merchantId) {
        Optional<Merchant> optional = repository.findById(merchantId);
        if ( optional.isPresent()) {
            log.error("Merchant found");
            return optional.get();
        }else{
            log.error("Merchant not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Merchant not found");
        }
    }
}
