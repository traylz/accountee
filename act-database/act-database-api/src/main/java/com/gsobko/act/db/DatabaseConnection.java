package com.gsobko.act.db;

public interface DatabaseConnection {
    <K, E extends Entity<K>, D extends Dao<K, E>> D getDao(Class<D> daoClass);
}
