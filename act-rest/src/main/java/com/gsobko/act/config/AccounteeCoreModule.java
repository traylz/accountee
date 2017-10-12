package com.gsobko.act.config;

import com.google.inject.AbstractModule;
import com.gsobko.act.AccountManager;
import com.gsobko.act.AccountManagerImpl;
import com.gsobko.act.TransferManager;
import com.gsobko.act.TransferManagerImpl;
import com.gsobko.act.db.Database;

public class AccounteeCoreModule extends AbstractModule {
    @Override
    protected void configure() {
        try {
            bind(AccountManager.class).toConstructor(AccountManagerImpl.class.getConstructor(Database.class));
            bind(TransferManager.class).toConstructor(TransferManagerImpl.class.getConstructor(Database.class));
        } catch (NoSuchMethodException e) {
            addError(e);
        }
    }
}
