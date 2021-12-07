package com.bigtree.fapi.controller;

import com.bigtree.fapi.entity.Company;
import com.bigtree.fapi.service.CompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAll(){
        log.info("Received request to get all companies");
        List<Company> companies = companyService.getAllCompanies();
        return ResponseEntity.ok().body(companies);
    }
    @GetMapping(value = "/companies/{companyId}")
    public ResponseEntity<Company> get(@PathVariable UUID companyId){
        log.info("Received request to get company {}", companyId);
        Company company = companyService.getCompany(companyId);
        return ResponseEntity.ok().body(company);
    }

    @PostMapping(value = "/companies", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Company> create(@Valid @RequestBody Company company){
        log.info("Received request to create new company {}", company);
        Company newCompany = companyService.saveCompany(company);
        return new ResponseEntity<>(newCompany, HttpStatus.CREATED);
    }

    @PutMapping(value = "/companies/{companyId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Company> update(@PathVariable UUID companyId, @Valid @RequestBody Company company){
        log.info("Received request to update company {}", companyId);
        Company updated = companyService.updateCompany(companyId, company);
        return ResponseEntity.ok().body(updated);
    }

    @DeleteMapping(value = "/companies/{companyId}")
    public ResponseEntity<Void> delete(@PathVariable UUID companyId){
        log.info("Received request to delete company {}", companyId);
        companyService.deleteCompany(companyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
