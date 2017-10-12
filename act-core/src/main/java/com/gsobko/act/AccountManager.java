package com.gsobko.act;

import com.gsobko.act.model.Account;

import java.math.BigDecimal;
import java.util.Collection;

public interface AccountManager {
    Account createAccount(BigDecimal initialAmount) throws ActUserException;
    Collection<Account> allAccounts();
}
