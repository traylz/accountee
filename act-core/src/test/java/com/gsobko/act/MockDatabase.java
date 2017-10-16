package com.gsobko.act;


import com.gsobko.act.db.Database;
import com.gsobko.act.db.DatabaseConnection;
import com.gsobko.act.db.TransactionCallback;

class MockDatabase implements Database {
    private final DatabaseConnection connection;


    MockDatabase(DatabaseConnection connection) {
        this.connection = connection;
    }

    @Override
    public <T> T doInTransaction(TransactionCallback<T> callback) {
        return callback.inTransaction(connection);
    }
}
