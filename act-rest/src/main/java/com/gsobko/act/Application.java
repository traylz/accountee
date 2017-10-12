package com.gsobko.act;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.gsobko.act.config.AccounteeCoreModule;
import com.gsobko.act.config.SimpleImDatabaseModule;
import com.gsobko.act.db.Database;

public class Application {
    public static void main(String[] args) throws Exception {
        Injector injector = Guice.createInjector(new SimpleImDatabaseModule(), new AccounteeCoreModule());

        AccounteeRestApp accounteeRestApp =
                new AccounteeRestApp(
                        injector.getInstance(TransferManager.class),
                        injector.getInstance(AccountManager.class),
                        injector.getInstance(Database.class));

        accounteeRestApp.runAsServer();
    }
}
