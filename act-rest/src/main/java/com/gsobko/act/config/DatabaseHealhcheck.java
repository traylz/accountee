package com.gsobko.act.config;

import com.google.inject.Inject;
import com.gsobko.act.db.Database;
import com.gsobko.act.db.JdbcDatabase;
import ru.vyarus.dropwizard.guice.module.installer.feature.health.NamedHealthCheck;

import static jersey.repackaged.com.google.common.collect.ImmutableList.of;

public class DatabaseHealhcheck extends NamedHealthCheck {

    private final Database database;

    @Inject
    public DatabaseHealhcheck(Database database) {
        this.database = database;
    }

    @Override
    protected Result check() throws Exception {
        if (database instanceof JdbcDatabase) {
            try {
                ((JdbcDatabase) database).execute(of("select 1 from dual"));
            } catch (Exception e) {
                Result.unhealthy(e);
            }
        }
        return Result.healthy();
    }

    @Override
    public String getName() {
        return "database";
    }
}
