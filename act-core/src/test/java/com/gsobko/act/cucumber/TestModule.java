package com.gsobko.act.cucumber;

import com.google.inject.AbstractModule;
import com.gsobko.act.AccountManager;
import com.gsobko.act.AccountManagerImpl;
import com.gsobko.act.TransferManager;
import com.gsobko.act.TransferManagerImpl;
import com.gsobko.act.db.Database;
import com.gsobko.act.db.SimpleInMemoryDatabaseImpl;

import static cucumber.api.guice.CucumberScopes.SCENARIO;

public class TestModule extends AbstractModule {
    @Override
    protected void configure() {
        try {

            bind(Database.class).toConstructor(SimpleInMemoryDatabaseImpl.class.getConstructor()).in(SCENARIO);
            bind(TestScenarioScope.class).in(SCENARIO);
            bind(AccountManager.class).toConstructor(AccountManagerImpl.class.getConstructor(Database.class)).in(SCENARIO);
            bind(TransferManager.class).toConstructor(TransferManagerImpl.class.getConstructor(Database.class)).in(SCENARIO);

        } catch (NoSuchMethodException e) {
            addError(e);
        }
    }
}
