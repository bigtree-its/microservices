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
import java.util.regex.Pattern;

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
        switch (account.getBank()) {
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

    private BigDecimal getAmount(String value) {
        if (StringUtils.isEmpty(value)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(value);
    }

    public Transaction toTransaction(ITransactionCsv transaction, UUID id) {
        Transaction entity = Transaction.builder()
                .accountId(id)
                .date(LocalDate.parse(transaction.getDate(), formatter))
                .type(transaction.getType())
                .code(transaction.getCode())
                .amount(transaction.getAmount())
                .balance(transaction.getBalance())
                .description(transaction.getDescription())
                .build();
        setCategory(transaction, entity);

        log.info("Transaction Entity: {}", entity);
        return entity;
    }

    private void setCategory(ITransactionCsv transaction, Transaction entity) {
        boolean accounting = Pattern.compile(Pattern.quote("ACCOUNTING"), Pattern.CASE_INSENSITIVE).matcher(transaction.getDescription()).find();
        boolean charges = Pattern.compile(Pattern.quote("CHARGES"), Pattern.CASE_INSENSITIVE).matcher(transaction.getDescription()).find();
        boolean pension = Pattern.compile(Pattern.quote("NEST"), Pattern.CASE_INSENSITIVE).matcher(transaction.getDescription()).find();
        boolean salary = Pattern.compile(Pattern.quote("SALARY"), Pattern.CASE_INSENSITIVE).matcher(transaction.getDescription()).find();
        boolean vat = Pattern.compile(Pattern.quote("VAT"), Pattern.CASE_INSENSITIVE).matcher(transaction.getDescription()).find();
        boolean paye = Pattern.compile(Pattern.quote("PAYE/NIC"), Pattern.CASE_INSENSITIVE).matcher(transaction.getDescription()).find();
        boolean dividend = Pattern.compile(Pattern.quote("DIVIDEND"), Pattern.CASE_INSENSITIVE).matcher(transaction.getDescription()).find();
        boolean hmrcJrsGrant = Pattern.compile(Pattern.quote("HMRC JRS GRANT"), Pattern.CASE_INSENSITIVE).matcher(transaction.getDescription()).find();
        boolean corpTax = Pattern.compile(Pattern.quote("CORP TAX"), Pattern.CASE_INSENSITIVE).matcher(transaction.getDescription()).find();
        boolean self = Pattern.compile(Pattern.quote("SELF"), Pattern.CASE_INSENSITIVE).matcher(transaction.getDescription()).find();
        boolean insurance = Pattern.compile(Pattern.quote("HISCOX"), Pattern.CASE_INSENSITIVE).matcher(transaction.getDescription()).find();
        boolean expense = Pattern.compile(Pattern.quote("Expense"), Pattern.CASE_INSENSITIVE).matcher(transaction.getDescription()).find();
        boolean loan = Pattern.compile(Pattern.quote("LOAN"), Pattern.CASE_INSENSITIVE).matcher(transaction.getDescription()).find();
        if (accounting) {
            entity.setCategory("ACCOUNTING");
        } else if (charges) {
            entity.setCategory("CHARGES");
        } else if (pension) {
            entity.setCategory("PENSION");
        } else if (salary) {
            entity.setCategory("SALARY");
        } else if (vat) {
            entity.setCategory("VAT");
        } else if (paye) {
            entity.setCategory("PAYE/NIC");
        } else if (dividend) {
            entity.setCategory("DIVIDEND");
        } else if (hmrcJrsGrant) {
            entity.setCategory("HMRC JRS GRANT");
        } else if (corpTax) {
            entity.setCategory("CORP TAX");
        } else if (self) {
            entity.setCategory("SELF ASSESSMENT");
        } else if (insurance) {
            entity.setCategory("INSURANCE");
        } else if (expense) {
            entity.setCategory("EXPENSE");
        } else if (loan) {
            entity.setCategory("LOAN");
        }
    }

}
