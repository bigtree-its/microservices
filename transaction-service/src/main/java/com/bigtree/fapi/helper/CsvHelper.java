package com.bigtree.fapi.helper;

import com.bigtree.fapi.entity.Account;
import com.bigtree.fapi.entity.Transaction;
import com.bigtree.fapi.model.HsbcBusinessCsv;
import com.bigtree.fapi.model.ITransactionCsv;
import com.bigtree.fapi.model.SantanderTransactionCsv;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Component
@Slf4j
public class CsvHelper {
    public static String TYPE = "text/csv";
    private static final DateTimeFormatterBuilder builder = new DateTimeFormatterBuilder().parseCaseInsensitive().parseLenient().appendPattern("[dd MMM yyyy]");
    private static final DateTimeFormatter formatter = builder.toFormatter(Locale.ENGLISH);

    public boolean hasCSVFormat(MultipartFile file) {
        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }

    public List<? extends ITransactionCsv> parseCsvFile(Account account, MultipartFile file) {

        List<? extends ITransactionCsv> transactions;
        switch (account.getBank()){
            case ApiConstants.Banks.HSBC:
                transactions = new CsvToBeanMapper<HsbcBusinessCsv>().map(file, HsbcBusinessCsv.class);
                break;
            case ApiConstants.Banks.SANTANDER:
                transactions = new CsvToBeanMapper<SantanderTransactionCsv>().map(file, SantanderTransactionCsv.class);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + account.getBank());
        }
        return transactions;
    }

    private BigDecimal getAmount(String value){
        if (StringUtils.isEmpty(value)){
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value);
    }

    public Transaction toTransaction(ITransactionCsv transaction, UUID id) {
        Transaction entity =  Transaction.builder()
                .accountId(id)
                .date(LocalDate.parse(transaction.getDate(), formatter))
                .type(transaction.getType())
                .code(transaction.getCode())
                .amount(transaction.getAmount())
                .description(transaction.getDescription())
                .build();
        log.info("Transaction Entity: {}", entity);
        return entity;
    }

}
