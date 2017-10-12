package com.gsobko.act;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Throwables;
import com.google.common.io.Resources;
import com.gsobko.act.config.AccounteeConfiguration;
import com.gsobko.act.config.InitialState;
import com.gsobko.act.db.AccountDao;
import com.gsobko.act.db.Dao;
import com.gsobko.act.db.Database;
import com.gsobko.act.model.Account;
import com.gsobko.act.rest.AccountsEndpoint;
import com.gsobko.act.rest.RuntimeExceptionMapper;
import com.gsobko.act.rest.TransferEndpoint;
import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import java.io.IOException;
import java.net.URL;

public class AccounteeRestApp extends Application<AccounteeConfiguration> {

    private final TransferManager transferManager;
    private final AccountManager accountManager;
    private final Database database;

    public AccounteeRestApp(TransferManager transferManager, AccountManager accountManager, Database database) {
        this.transferManager = transferManager;
        this.accountManager = accountManager;
        this.database = database;
    }

    @Override
    public void initialize(Bootstrap<AccounteeConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());
    }

    @Override
    public void run(AccounteeConfiguration accounteeConfiguration, Environment environment) throws Exception {
        environment.jersey().register(new RuntimeExceptionMapper());
        environment.jersey().register(new TransferEndpoint(transferManager));
        environment.jersey().register(new AccountsEndpoint(accountManager));
        ObjectMapper objectMapper = environment.getObjectMapper();

        if (accounteeConfiguration.getStartWithInitialState()) {
            createInitialState(accounteeConfiguration.getInitialStateUri(), objectMapper);
        }
    }

    private void createInitialState(String initialStateUri, ObjectMapper objectMapper) {
        URL resource = Resources.getResource(initialStateUri);
        try {
            InitialState initialState = objectMapper.readValue(resource, InitialState.class);

            database.doInTransaction(
                    connection -> {
                        Dao<Long, Account> accountDao = connection.getDao(AccountDao.class);
                        for (InitialState.Account account : initialState.getAccounts()) {
                            accountDao.create(new Account(null, account.getAmount()));
                        }
                        return null;
                    }
            );

        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }

    public void runAsServer() throws Exception {
        run("server", "accountee.yaml");
    }
}
