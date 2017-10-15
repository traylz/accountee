package com.gsobko.act.db;

import com.gsobko.act.model.Transfer;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

class JdbcTransferDaoImpl implements TransferDao {

    private static final String SELECT_ALL = "SELECT id, amount, fromId, toId FROM transfer";
    private static final String INSERT = "INSERT INTO transfer (id, amount, fromId, toId) VALUES (?, ?, ?, ?)";
    private final Connection connection;

    JdbcTransferDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Optional<Transfer> find(String byKey) {
        throw new UnsupportedOperationException("find of transaction is unsupported");
    }

    @Override
    public Optional<Transfer> findForUpdate(String byKey) {
        throw new UnsupportedOperationException("find for update of transaction is unsupported");
    }

    @Override
    public Transfer update(Transfer entity) {
        throw new UnsupportedOperationException("update of transaction is unsupported");
    }

    @Override
    public Transfer create(Transfer entity) {
        try (PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getKey());
            statement.setBigDecimal(2, entity.getAmount());
            statement.setLong(3, entity.getFrom());
            statement.setLong(4, entity.getTo());
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            if (e.getErrorCode() == 23505) {
                throw new DuplicateKeyViolationException("Unique key violation, transfer with id = " + entity.getKey() + " already exists");
            }
            throw new DaoExcepton(e);
        }

    }

    @Override
    public void delete(String byKey) {
        // TODO implement - skipping this to save some time (as not used)
        throw new UnsupportedOperationException("delete of account is not implemented");
    }

    @Override
    public Collection<Transfer> getAll() {
        List<Transfer> transfers = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    transfers.add(new Transfer(resultSet.getString("id"), resultSet.getBigDecimal("amount"),
                            resultSet.getLong("fromId"), resultSet.getLong("toId")));
                }
                return transfers;
            }
        } catch (SQLException e) {
            throw new DaoExcepton(e);
        }
    }
}
