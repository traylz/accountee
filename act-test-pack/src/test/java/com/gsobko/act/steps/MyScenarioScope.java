package com.gsobko.act.steps;

import com.gsobko.act.model.Account;

import java.util.HashMap;
import java.util.Map;

public class MyScenarioScope {
    private final Map<String, Account> accountAliases = new HashMap<>();

    void registerAlias(String alias, Account account) {
        accountAliases.put(alias, account);
    }

    Account getByAlias(String alias) {
        return accountAliases.get(alias);
    }
}
