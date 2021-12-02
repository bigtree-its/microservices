package com.bigtree.fapi.helper;

import com.bigtree.fapi.error.ApiException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

@Slf4j
public class CsvToBeanMapper<T> {

    public List<T> map(MultipartFile file, Class<? extends T> type){
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        }catch (Exception ex) {
            log.error("Unable to process CSV file. {}", ex.getMessage());
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Unable to process CSV file");
        }

    }
}
