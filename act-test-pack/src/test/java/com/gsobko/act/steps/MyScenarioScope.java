package com.gsobko.act.steps;

import com.gsobko.act.model.Account;
import com.mashape.unirest.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class MyScenarioScope {
    private final Map<String, Account> accountAliases = new HashMap<>();
    private final Map<String, String> tokens = new HashMap<>();
    private HttpResponse<String> response;

    void registerAlias(String alias, Account account) {
        tokens.put(alias, account.getKey().toString());
        accountAliases.put(alias, account);
    }

    void registerToken(String token, String value) {
        tokens.put(token, value);
    }

    Account getByAlias(String alias) {
        return accountAliases.get(alias);
    }

    public Map<String, String> getTokens() {
        return tokens;
    }

    public void registerResponse(HttpResponse<String> response) {
        this.response = response;
    }

    public HttpResponse<String> getResponse() {
        return response;
    }
}
