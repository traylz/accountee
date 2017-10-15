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

                    Dao<String, Transfer> transferDao = connection.getDao(TransferDao.class);
                    Transfer transfer = new Transfer(nonNullTransferId, amount, accountFrom, accountTo);
                    try {
                        transferDao.create(transfer);
                    } catch (DuplicateKeyViolationException e) {
                        throw new ActUserException("Transfer '" + nonNullTransferId + "' was already performed", e);
                    }


                    if (amount.signum() <= 0) {
                        throw new ActUserException("Transfer amount should be positive, but got " + amount);
                    }

                    AccountDao accountDao = connection.getDao(AccountDao.class);

                    final Account from;
                    final Account to;
                    // ordering to avoid deadlocks
                    if (accountFrom < accountTo) {
                        from = getAccountForUpdate(accountFrom, accountDao);
                        to = getAccountForUpdate(accountTo, accountDao);
                    } else {
                        to = getAccountForUpdate(accountTo, accountDao);
                        from = getAccountForUpdate(accountFrom, accountDao);
                    }

                    to.setAmount(to.getAmount().add(amount));
                    if (from.getAmount().subtract(amount).signum() < 0) {
                        throw new ActUserException("Not enough funds on account to transfer " + amount);
                    }

                    from.setAmount(from.getAmount().subtract(amount));

                    accountDao.update(to);
                    accountDao.update(from);

                    return transfer;
                }
        );
    }

    @Override
    public Collection<Transfer> allTransfers() {
        return database.doInTransaction(databaseConnection ->
                databaseConnection.getDao(TransferDao.class).getAll());
    }

    private static Account getAccountForUpdate(Long accountId, AccountDao accountDao) {
        return accountDao.findForUpdate(accountId)
                .orElseThrow(() -> new ActUserException("Cannot find account by id " + accountId));
    }

}
