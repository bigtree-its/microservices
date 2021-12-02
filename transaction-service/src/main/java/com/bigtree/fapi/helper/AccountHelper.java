package com.bigtree.fapi.helper;

import com.bigtree.fapi.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class AccountHelper {

    public boolean isValidBank(Account account){
        return Arrays.asList(ApiConstants.Banks.HSBC, ApiConstants.Banks.SANTANDER).contains(account.getBank());
    }
}
