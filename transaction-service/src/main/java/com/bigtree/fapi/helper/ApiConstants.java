package com.bigtree.fapi.helper;

public class ApiConstants {

    public static final String FAPI_DATE_FORMAT = "dd/MMM/yyyy";

    public static class AccountType {
        public static final String BUSINESS = "Business";
        public static final String PERSONAL = "Personal";
    }

    public static class TransactionType {
        public static final String DEBIT = "Debit";
        public static final String Credit = "Credit";
    }

    public static class Banks{
        public static final String HSBC="HSBC";
        public static final String SANTANDER="Santander";
        public static final String LLOYDS="LLoyds";
        public static final String NATWEST="Natwest";
        public static final String MONZO="Monzo";
        public static final String STARLING_BANK="Starling";
    }
}
