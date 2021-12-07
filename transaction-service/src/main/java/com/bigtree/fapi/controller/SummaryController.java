package com.bigtree.fapi.controller;

import com.bigtree.fapi.error.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@Slf4j
public class SummaryController {

    @GetMapping("/api/summary/v1/{companyId}")
    public ResponseEntity getSummary(@PathVariable(name = "companyId") UUID companyId,
                                     @RequestHeader(name = "FY") String fy){
        log.info("Received request get summary for the company {} FY {}", companyId, fy);
        try{
            String[] years = fy.split("-");
            int yearStart = Integer.parseInt(years[0]);
            int yearEnd = Integer.parseInt(years[1]);

        } catch (Exception e){
            throw new ApiException(HttpStatus.BAD_REQUEST, "FY should be in YYYY-YYYY format");
        }
       return ResponseEntity.ok("Summary");
    }
}
