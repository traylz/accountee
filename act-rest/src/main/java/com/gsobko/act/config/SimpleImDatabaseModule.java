package com.gsobko.act.config;

import com.google.inject.AbstractModule;
import com.gsobko.act.db.Database;
import com.gsobko.act.db.SimpleInMemoryDatabaseImpl;

public class SimpleImDatabaseModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Database.class).toInstance(new SimpleInMemoryDatabaseImpl());
    }

}
