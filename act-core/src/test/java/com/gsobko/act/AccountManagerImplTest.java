package com.gsobko.act;

import com.gsobko.act.db.AccountDao;
import com.gsobko.act.db.DatabaseConnection;
import com.gsobko.act.model.Account;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class AccountManagerImplTest {

    private AccountDao accountDao;
    private AccountManager accountManager;

    @Before
    public void setUp() throws Exception {
        DatabaseConnection connection = mock(DatabaseConnection.class);
        accountDao = mock(AccountDao.class);
        when(connection.getDao(AccountDao.class)).thenReturn(accountDao);
        accountManager = new AccountManagerImpl(new MockDatabase(connection));
    }

    @Test
    public void daoCreateShouldBeCalledWithoutKey() throws Exception {
        Account expectedAccount = new Account(5L, BigDecimal.ONE);
        when(accountDao.create(any())).thenReturn(expectedAccount);

        Account account = accountManager.createAccount(BigDecimal.ONE);

        assertEquals(expectedAccount, account);
        Mockito.verify(accountDao).create(eq(new Account(null, BigDecimal.ONE)));
    }

    @Test
    public void findAllShouldCallFindAllOfAccountDao() throws Exception {
        List<Account> accounts = Collections.emptyList();
        when(accountDao.getAll()).thenReturn(accounts);

        Collection<Account> foundAccounts = accountManager.allAccounts();

        assertEquals(accounts, foundAccounts);
        verify(accountDao).getAll();
    }

    @Test
    public void findShouldCallFindOfAccountDao() throws Exception {
        Optional<Account> optional = Optional.of(new Account(1L, BigDecimal.ZERO));
        when(accountDao.find(eq(1L))).thenReturn(optional);

        Optional<Account> account = accountManager.findAccount(1L);

        assertEquals(optional, account);
        verify(accountDao).find(1L);
    }

}