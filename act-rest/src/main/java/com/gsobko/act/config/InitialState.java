package com.gsobko.act.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class InitialState {
    @NotNull
    private List<Account> accounts;

    @JsonProperty
    public List<Account> getAccounts() {
        return accounts;
    }

    @JsonProperty
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public static class Account {
        private BigDecimal amount;


        @JsonProperty
        public BigDecimal getAmount() {
            return amount;
        }

        @JsonProperty
        public void setAmount(BigDecimal amount) {
            this.amount = amount;
        }
    }
}
