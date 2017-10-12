package com.gsobko.act.db;

public interface Database {
    <T> T doInTransaction(TransactionCallback<T> callback);
}
