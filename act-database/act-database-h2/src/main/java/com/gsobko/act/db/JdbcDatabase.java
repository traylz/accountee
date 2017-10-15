package com.gsobko.act.db;

import com.google.common.collect.ImmutableMap;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class JdbcDatabase implements Database {

    private final DataSource dataSource;
    private final Map<Class<? extends Dao<?, ?>>, Function<Connection, Dao<?, ?>>> daoProvider;

    public JdbcDatabase(DataSource dataSource) {
        this.dataSource = dataSource;
        daoProvider = ImmutableMap.of(
                AccountDao.class, JdbcAccountDaoImpl::new,
                TransferDao.class, JdbcTransferDaoImpl::new
        );
    }

    public <T> T doInTransaction(TransactionCallback<T> callback) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                T result = callback.inTransaction(new JdbcDaoProvider(connection));
                connection.commit();
                return result;
            } catch (RuntimeException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new DaoExcepton(e);
        }
    }

    public void execute(List<String> sqls) {
        try (Connection connection = dataSource.getConnection()) {
            try {
                for (String sql : sqls) {
                    try (PreparedStatement ps = connection.prepareStatement(sql)) {
                        ps.execute();
                    }
                }
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private class JdbcDaoProvider implements DatabaseConnection {
        private final Connection connection;

        private JdbcDaoProvider(Connection connection) {
            this.connection = connection;
        }

        public <K, E extends Entity<K>, D extends Dao<K, E>> D getDao(Class<D> daoClass) {
            return (D) daoProvider.get(daoClass).apply(connection);
        }
    }
}