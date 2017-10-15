package com.gsobko.act;

import com.gsobko.act.model.Account;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Optional;

public interface AccountManager {
    Account createAccount(BigDecimal initialAmount) throws ActUserException;
    Optional<Account> findAccount(Long id);
    Collection<Account> allAccounts();
}
