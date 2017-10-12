package com.gsobko.act;

import com.gsobko.act.db.AccountDao;
import com.gsobko.act.db.Database;
import com.gsobko.act.model.Account;

import java.math.BigDecimal;
import java.util.Collection;

public class AccountManagerImpl implements AccountManager {

    private final Database database;

    public AccountManagerImpl(Database database) {
        this.database = database;
    }

    @Override
    public Account createAccount(BigDecimal initialAmount) {
        return database.doInTransaction(connection ->
                connection.getDao(AccountDao.class).create(new Account(null, initialAmount)));
    }

    @Override
    public Collection<Account> allAccounts() {
        return database.doInTransaction(
                connection -> connection.getDao(AccountDao.class).getAll()
        );
    }
}
