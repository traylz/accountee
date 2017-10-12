package com.gsobko.act;

import com.gsobko.act.db.*;
import com.gsobko.act.model.Account;
import com.gsobko.act.model.Transfer;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.UUID;

public class TransferManagerImpl implements TransferManager {
    private final Database database;

    public TransferManagerImpl(Database database) {
        this.database = database;
    }

    @Override
    public Transfer doTransfer(Long accountFrom, Long accountTo, BigDecimal amount, String transferId) {
        final String nonNullTransferId = transferId != null ? transferId : UUID.randomUUID().toString();
        return database.doInTransaction(
                connection -> {
                    Dao<Long, Account> accountDao = connection.getDao(AccountDao.class);
                    Dao<String, Transfer> transferDao = connection.getDao(TransferDao.class);

                    // ToDo probably should check for duplicate TX first
                    if (amount.signum() <= 0) {
                        throw new ActUserException("Transfer amount should be positive, but got " + amount);
                    }


                    final Account from;
                    final Account to;
                    if (accountFrom < accountTo) {
                        from = decreaseSource(accountFrom, amount, accountDao);
                        to = increaseTarget(accountTo, amount, accountDao);
                    } else {
                        to = increaseTarget(accountTo, amount, accountDao);
                        from = decreaseSource(accountFrom, amount, accountDao);
                    }

                    Transfer transfer = new Transfer(nonNullTransferId, amount, from.getKey(), to.getKey());
                    try {
                        transferDao.create(transfer);
                    } catch (DuplicateKeyViolationException e) {
                        throw new ActUserException("Transfer '" + nonNullTransferId + "' was already performed", e);
                    }
                    return transfer;
                }
        );
    }

    @Override
    public Collection<Transfer> allTransfers() {
        return database.doInTransaction(databaseConnection ->
                databaseConnection.getDao(TransferDao.class).getAll());
    }

    private static Account increaseTarget(Long accountTo, BigDecimal amount, Dao<Long, Account> accountDao) {
        Account to = getAccountForUpdate(accountTo, accountDao);
        to.setAmount(to.getAmount().add(amount));
        return to;
    }


    private static Account decreaseSource(Long accountFrom, BigDecimal amount, Dao<Long, Account> accountDao) {
        Account from = getAccountForUpdate(accountFrom, accountDao);

        if (from.getAmount().subtract(amount).signum() < 0) {
            throw new ActUserException("Not enough funds on account to transfer " + amount);
        }

        from.setAmount(from.getAmount().subtract(amount));
        return from;
    }

    private static Account getAccountForUpdate(Long accountId, Dao<Long, Account> accountDao) {
        return accountDao.findForUpdate(accountId)
                .orElseThrow(() -> new ActUserException("Cannot find account by id " + accountId));
    }

}
