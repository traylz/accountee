package com.gsobko.act.db;

import com.gsobko.act.model.Account;

import java.util.concurrent.atomic.AtomicLong;

public class AccountDaoImpl extends AbstractInMemoryDao<Long, Account> implements AccountDao {

    private final AtomicLong sequence = new AtomicLong(1);

    @Override
    protected boolean isSequencedKey() {
        return true;
    }

    @Override
    protected Account applySequence(Account entity) {
        return new Account(sequence.getAndIncrement(), entity.getAmount());
    }
}
