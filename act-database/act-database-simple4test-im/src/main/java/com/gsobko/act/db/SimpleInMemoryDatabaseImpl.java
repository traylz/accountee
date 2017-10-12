package com.gsobko.act.db;

import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Dummy implementation - stores state in:
 * {class of entity => {key => entity}}
 * It handles transactions really simple (and stupid) but safe:
 * 1. globally locks on "doInTransaction"
 * 2. passes copy of state to trxCallback
 * 3. If trx committed - replaces current state wih new one.
 * Uses copy-on-write principle.
 * Represents serializable transaction isolation level
 */
public class SimpleInMemoryDatabaseImpl implements Database {

    // Lock instead of synchronized to be able to add timeout to waiting for connection
    private final Lock globalLock = new ReentrantLock();
    private int nestingLevel = 0;
    private Map<Class<? extends Dao>, AbstractInMemoryDao<?, ?>> daoMap;

    public SimpleInMemoryDatabaseImpl() {
        daoMap = ImmutableMap.of(
                AccountDao.class, new AccountDaoImpl(),
                TransferDao.class, new TransferDaoImpl()
        );
    }

    @Override
    public <T> T doInTransaction(TransactionCallback<T> callback) {
        globalLock.lock();
        nestingLevel++;
        if (nestingLevel > 1) {
            nestingLevel = 0;
            throw new DaoExcepton("Nested transactions are not supported");
        }
        try {
            T result = callback.inTransaction(new Connection());
            commit();
            return result;
        } catch (RuntimeException e) {
            rollback();
            throw e;
        } finally {
            nestingLevel--;
            globalLock.unlock();
        }
    }

    private void commit() {
        for (AbstractInMemoryDao<?, ?> dao : daoMap.values()) {
            dao.commit();
        }
    }

    private void rollback() {
        for (AbstractInMemoryDao<?, ?> dao : daoMap.values()) {
            dao.rollback();
        }
    }


    private class Connection implements DatabaseConnection {

        @Override
        public <K, E extends Entity<K>, D extends Dao<K, E>> D getDao(Class<D> daoClass) {
            return (D) daoMap.get(daoClass);
        }
    }
}
