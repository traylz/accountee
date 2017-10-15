package com.gsobko.act.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gsobko.act.model.Account;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class AccounteeConfiguration extends Configuration {

    @NotNull
    private Database database;

    private InitialState initialState;

    @JsonProperty
    public Database getDatabase() {
        return database;
    }

    @JsonProperty
    public void setDatabase(Database database) {
        this.database = database;
    }

    @JsonProperty
    public InitialState getInitialState() {
        return initialState;
    }

    @JsonProperty
    public void setInitialState(InitialState initialState) {
        this.initialState = initialState;
    }

    public static class Database {

        public enum DatabaseType {
            TEST, H2
        }

        @NotNull
        private DatabaseType type = DatabaseType.TEST;

        private String jdbcUrl;

        private List<String> initialSql;

        @JsonProperty
        public List<String> getInitialSql() {
            return initialSql;
        }

        @JsonProperty
        public void setInitialSql(List<String> initialSql) {
            this.initialSql = initialSql;
        }

        @JsonProperty
        public DatabaseType getType() {
            return type;
        }

        @JsonProperty
        public void setType(DatabaseType type) {
            this.type = type;
        }

        @JsonProperty
        public String getJdbcUrl() {
            return jdbcUrl;
        }

        @JsonProperty
        public void setJdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
        }
    }

    public static class InitialState {
        @NotNull
        private List<InitialAccount> initialAccounts;

        @JsonProperty
        public List<InitialAccount> getInitialAccounts() {
            return initialAccounts;
        }

        @JsonProperty
        public void setAccounts(List<InitialAccount> initialAccounts) {
            this.initialAccounts = initialAccounts;
        }


        public static class InitialAccount {
            private BigDecimal amount;
            private Long id;

            @JsonProperty
            public BigDecimal getAmount() {
                return amount;
            }

            @JsonProperty
            public void setAmount(BigDecimal amount) {
                this.amount = amount;
            }

            @JsonProperty
            public Long getId() {
                return id;
            }

            @JsonProperty
            public void setId(Long id) {
                this.id = id;
            }

            public Account toAccount() {
                return new Account(id, amount);
            }
        }
    }

}
