package com.bigtree.fapi.service;

import com.bigtree.fapi.entity.Company;
import com.bigtree.fapi.error.ApiException;
import com.bigtree.fapi.repository.CompanyRepository;
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
public class CompanyService {

    @Autowired
    CompanyRepository repository;

    @Transactional
    public Company saveCompany(Company company){
        log.info("Saving new company");
        Company exist = repository.findByCompanyNumber(company.getNumber());
        if ( exist != null && exist.getId() != null){
            log.error("Company already exist");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Company already exist");
        }
        Company save = repository.save(company);
        if ( save != null && save.getId() != null){
            log.info("Company created {}", save.getId());
        }
        return save;
    }

    public List<Company> getAllCompanies(){
        log.info("Fetching all Companies");
        return repository.findAll();
    }

    public Company updateCompany(UUID id, Company company) {
        Optional<Company> optional = repository.findById(id);
        if ( optional.isPresent()){
           log.info("Company already exist. Updating");
            Company exist = optional.get();
            if (StringUtils.hasLength(company.getName())){
                exist.setName(company.getName());
            }
            if (StringUtils.hasLength(company.getType())){
                exist.setType(company.getType());
            }
            if (StringUtils.hasLength(company.getStatus())){
                exist.setStatus(company.getStatus());
            }
            if (StringUtils.hasLength(company.getNatureOfBusiness())){
                exist.setNatureOfBusiness(company.getNatureOfBusiness());
            }
            if (company.getIncorporatedOn() != null){
                exist.setIncorporatedOn(company.getIncorporatedOn());
            }
            if (company.getRegisteredAddress() != null){
                exist.setRegisteredAddress(company.getRegisteredAddress());
            }
            if (company.getPeoples() != null){
                exist.setPeoples(company.getPeoples());
            }
            Company updated = repository.save(exist);
            if ( updated != null && updated.getId() != null){
                log.info("Company updated {}", updated.getId());
            }
            return updated;
        }else{
            throw new ApiException(HttpStatus.BAD_REQUEST, "Company not exist");
        }

    }

    public void deleteCompany(UUID companyId) {
        Optional<Company> optional = repository.findById(companyId);
        if ( optional.isPresent()) {
            log.info("Company already exist. Deleting company");
            repository.deleteById(companyId);
        }else{
            log.error("Company not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Company not found");
        }
    }

    public Company getCompany(UUID companyId) {
        Optional<Company> optional = repository.findById(companyId);
        if ( optional.isPresent()) {
            log.error("Company found");
            return optional.get();
        }else{
            log.error("Company not found");
            throw new ApiException(HttpStatus.BAD_REQUEST, "Company not found");
        }
    }
}
