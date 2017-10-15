package com.gsobko.act.cucumber.steps;

import com.google.inject.Inject;
import com.gsobko.act.db.AccountDao;
import com.gsobko.act.db.Database;
import com.gsobko.act.model.Account;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;

import static org.junit.Assert.assertEquals;

public class DatabaseSteps {

    @Inject
    private Database database;


    @Given("^Database contains following accounts:$")
    public void databaseContainFollowingAccounts(DataTable dataTable) throws Throwable {
        for (Account account : dataTable.asList(Account.class)) {
            database.doInTransaction(databaseConnection -> databaseConnection.getDao(AccountDao.class).create(account));
        }
    }

    @Then("^Database should contain following accounts:$")
    public void databaseShouldContainFollowingAccounts(DataTable dataTable) throws Throwable {
        for (Account account : dataTable.asList(Account.class)) {
            Account databaseAccount = database.doInTransaction(connection -> connection.getDao(AccountDao.class).find(account.getKey())).get();
            assertEquals(account, databaseAccount);
        }
    }
}
