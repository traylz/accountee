package com.gsobko.act.db;

import com.gsobko.act.model.Account;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkState;

class JdbcAccountDaoImpl implements AccountDao {

    private static final String SELECT = "SELECT id, amount FROM account WHERE id = ?";
    private static final String SELECT_ALL = "SELECT id, amount FROM account";
    private static final String SELECT_FOR_UPD = "SELECT id, amount FROM account WHERE id = ? FOR UPDATE";
    private static final String UPDATE = "UPDATE account SET amount = ? WHERE id = ?";
    private static final String INSERT = "INSERT INTO account (amount) VALUES (?)";
    private final Connection connection;

    JdbcAccountDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Account> find(Long byKey) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT)) {
            statement.setLong(1, byKey);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Account(resultSet.getLong("id"), resultSet.getBigDecimal("amount")));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DaoExcepton(e);
        }
    }

    @Override
    public Optional<Account> findForUpdate(Long byKey) {
        try (PreparedStatement statement = connection.prepareStatement(SELECT_FOR_UPD)) {
            statement.setLong(1, byKey);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(new Account(resultSet.getLong("id"), resultSet.getBigDecimal("amount")));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DaoExcepton(e);
        }
    }

    @Override
    public Account update(Account entity) {
        try (PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setBigDecimal(1, entity.getAmount());
            statement.setLong(2, entity.getKey());
            int rowCount = statement.executeUpdate();
            if (rowCount != 1) {
                throw new DaoExcepton("Couldn't find account for update with id " + entity.getKey());
            }
            return entity;
        } catch (SQLException e) {
            throw new DaoExcepton(e);
        }
    }

    @Override
    public Account create(Account entity) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setBigDecimal(1, entity.getAmount());
            statement.executeUpdate();
            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                checkState(resultSet.next());
                return new Account(resultSet.getLong(1), entity.getAmount());
            }
        } catch (SQLException e) {
            throw new DaoExcepton(e);
        }

    }

    @Override
    public void delete(Long byKey) {
        // TODO implement - skipping this to save some time (as not used)
        throw new UnsupportedOperationException("delete of account is not implemented");
    }


    @Override
    public Collection<Account> getAll() {
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    accounts.add(new Account(resultSet.getLong("id"), resultSet.getBigDecimal("amount")));
                }
                return accounts;
            }
        } catch (SQLException e) {
            throw new DaoExcepton(e);
        }
    }
}
