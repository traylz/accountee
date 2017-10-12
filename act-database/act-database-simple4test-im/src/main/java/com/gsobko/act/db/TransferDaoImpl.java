package com.gsobko.act.db;

import com.gsobko.act.model.Transfer;

public class TransferDaoImpl extends AbstractInMemoryDao<String, Transfer> implements TransferDao {

    @Override
    protected boolean isSequencedKey() {
        return false;
    }

    @Override
    protected Transfer applySequence(Transfer entity) {
        return null;
    }
}
