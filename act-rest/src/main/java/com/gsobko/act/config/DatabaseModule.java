package com.gsobko.act.config;

import com.gsobko.act.config.AccounteeConfiguration.Database.DatabaseType;
import com.gsobko.act.db.AccountDao;
import com.gsobko.act.db.Database;
import com.gsobko.act.db.JdbcDatabase;
import com.gsobko.act.db.SimpleInMemoryDatabaseImpl;
import org.h2.jdbcx.JdbcConnectionPool;
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule;

import javax.sql.DataSource;
import java.sql.Connection;

public class DatabaseModule extends DropwizardAwareModule<AccounteeConfiguration> {
    @Override
    protected void configure() {
        AccounteeConfiguration.Database dbConfig = configuration().getDatabase();
        DatabaseType databaseType = dbConfig.getType();
        Database database = null;
        if (databaseType == DatabaseType.TEST) {
            database = new SimpleInMemoryDatabaseImpl();
        } else if (databaseType == DatabaseType.H2) {
            DataSource ds = initDatasource(dbConfig.getJdbcUrl());
            JdbcDatabase jdbcDatabase = new JdbcDatabase(ds);
            if (dbConfig.getInitialSql() != null) {
                jdbcDatabase.execute(dbConfig.getInitialSql());
            }
            database = jdbcDatabase;
        } else {
            addError("Unsupported database type : " + databaseType);
        }
        bind(Database.class).toInstance(database);
        if (configuration().getInitialState() != null) {
            for (AccounteeConfiguration.InitialState.InitialAccount account : configuration().getInitialState().getInitialAccounts()) {
                database.doInTransaction(
                        conn -> conn.getDao(AccountDao.class).create(account.toAccount())
                );
            }
        }


    }

    private DataSource initDatasource(String jdbcUrl) {
        JdbcConnectionPool cp = JdbcConnectionPool.
                create("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "sa");
        return cp;
    }

}
