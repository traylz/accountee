package com.gsobko.act.db;

@FunctionalInterface
public interface TransactionCallback<T> {

    /**
     * This method allows you to do some database manipulation in transaction.
     * If exception is thrown from this method then transaction will be rolled back.
     * If method executes successfully transaction will be committed.
     * @param databaseConnection database connection
     */
    T inTransaction(DatabaseConnection databaseConnection);

}
