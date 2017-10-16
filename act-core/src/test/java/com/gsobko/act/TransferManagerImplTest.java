package com.gsobko.act;

import com.gsobko.act.db.AccountDao;
import com.gsobko.act.db.DatabaseConnection;
import com.gsobko.act.db.DuplicateKeyViolationException;
import com.gsobko.act.db.TransferDao;
import com.gsobko.act.model.Account;
import com.gsobko.act.model.Transfer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TransferManagerImplTest {

    private AccountDao accountDao;
    private TransferDao transferDao;
    private TransferManager transferManager;

    @Before
    public void setUp() throws Exception {
        DatabaseConnection connection = mock(DatabaseConnection.class);
        accountDao = mock(AccountDao.class);
        transferDao = mock(TransferDao.class);
        when(connection.getDao(AccountDao.class)).thenReturn(accountDao);
        when(connection.getDao(TransferDao.class)).thenReturn(transferDao);
        transferManager = new TransferManagerImpl(new MockDatabase(connection));
    }

    @Test(expected = ActUserException.class)
    public void transferManagerShouldRejectDuplicateTransfer() throws Exception {
        when(transferDao.create(any())).thenThrow(DuplicateKeyViolationException.class);
        transferManager.doTransfer(1L, 2L, BigDecimal.ONE, "123");
    }

    @Test(expected = ActUserException.class)
    public void transferManagerShouldRejectNegativeTransfer() throws Exception {
        when(transferDao.create(any())).thenReturn(new Transfer("123", BigDecimal.ONE, 1L, 2L));
        transferManager.doTransfer(1L, 2L, new BigDecimal(-1L), "123");
    }


    @Test(expected = ActUserException.class)
    public void transferManagerShouldRejectNonFoundAccountTransfer() throws Exception {
        when(transferDao.create(any())).thenReturn(mock(Transfer.class));
        when(accountDao.findForUpdate(eq(1L))).thenReturn(Optional.empty());
        transferManager.doTransfer(1L, 2L, new BigDecimal(-1L), "123");
    }


    @Test
    public void transferManagerShouldCallFindForUpdateInOrder1() throws Exception {

        when(transferDao.create(any())).thenReturn(mock(Transfer.class));
        when(accountDao.findForUpdate(1L)).thenReturn(Optional.of(new Account(1L, BigDecimal.valueOf(100))));
        when(accountDao.findForUpdate(2L)).thenReturn(Optional.of(new Account(2L, BigDecimal.valueOf(100))));

        transferManager.doTransfer(1L, 2L, BigDecimal.ONE, "123");

        InOrder daoInOrder = Mockito.inOrder(accountDao);
        daoInOrder.verify(accountDao).findForUpdate(eq(1L));
        daoInOrder.verify(accountDao).findForUpdate(eq(2L));
    }

    @Test
    public void transferManagerShouldCallFindForUpdateInOrder2() throws Exception {

        when(transferDao.create(any())).thenReturn(mock(Transfer.class));
        when(accountDao.findForUpdate(1L)).thenReturn(Optional.of(new Account(1L, BigDecimal.valueOf(100))));
        when(accountDao.findForUpdate(2L)).thenReturn(Optional.of(new Account(2L, BigDecimal.valueOf(100))));

        transferManager.doTransfer(2L, 1L, BigDecimal.ONE, "123");

        InOrder daoInOrder = Mockito.inOrder(accountDao);
        daoInOrder.verify(accountDao).findForUpdate(eq(1L));
        daoInOrder.verify(accountDao).findForUpdate(eq(2L));
    }

    @Test(expected = ActUserException.class)
    public void transferManagerShouldTrowExceptionOnInsufficientFunds() throws Exception {
        when(transferDao.create(any())).thenReturn(mock(Transfer.class));
        BigDecimal amountOnAccount1 = BigDecimal.valueOf(100);
        when(accountDao.findForUpdate(1L)).thenReturn(Optional.of(new Account(1L, amountOnAccount1)));
        when(accountDao.findForUpdate(2L)).thenReturn(Optional.of(new Account(2L, BigDecimal.ZERO)));

        transferManager.doTransfer(1L, 2L, amountOnAccount1.add(BigDecimal.ONE), "123");
    }

    @Test
    public void listAllShouldCallDaoGetAll() throws Exception {
        List<Transfer> transfers = Collections.emptyList();
        when(transferDao.getAll()).thenReturn(transfers);

        Collection<Transfer> result = transferManager.allTransfers();

        assertEquals(transfers, result);
        Mockito.verify(transferDao).getAll();

    }


}