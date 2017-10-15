package com.gsobko.act.db;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

//@NotThreadSafe
abstract class AbstractInMemoryDao<K, E extends Entity<K>> implements Dao<K, E> {

    private Map<K, E> state;
    private Map<K, E> stateCandidate;

    protected AbstractInMemoryDao() {
        this.state = ImmutableMap.of();
    }

    @Override
    public Optional<E> find(K byKey) {
        return Optional.ofNullable(getState().get(byKey)).map(this::cloneEntity);
    }

    @Override
    public Optional<E> findForUpdate(K byKey) {
        return Optional.ofNullable(getStateToModify().get(byKey)).map(this::cloneEntity);
    }

    @Override
    public E update(E entity) {
        checkEntityExists(entity.getKey());
        getStateToModify().put(entity.getKey(), entity);
        return cloneEntity(entity);
    }

    @Override
    public E create(E entity) {
        if (isSequencedKey() && entity.getKey() == null) {
            entity = applySequence(entity);
        }
        if (entity.getKey() == null) {
            throw new DaoExcepton("Cannot save entity with null key");
        }
        if (getStateToModify().get(entity.getKey()) != null) {
            throw new DuplicateKeyViolationException("Entity with key " + entity.getKey() + " already exists");
        }
        getStateToModify().put(entity.getKey(), entity);
        return cloneEntity(entity);
    }

    @Override
    public void delete(K byKey) {
        checkEntityExists(byKey);
        getStateToModify().remove(byKey);
    }

    @Override
    public Collection<E> getAll() {
        return getState().values().stream().map(this::cloneEntity).collect(Collectors.toList());
    }

    private E cloneEntity(E ent) {
        return (E) ent.clone();
    }

    protected abstract boolean isSequencedKey();

    protected abstract E applySequence(E entity);


    private void checkEntityExists(K key) {
        if (getStateToModify().get(key) == null) {
            throw new DaoExcepton("Cannot find entity to update by key " + key);
        }
    }

    protected void rollback() {
        stateCandidate = null;
    }

    protected void commit() {
        if (stateCandidate != null) {
            state = ImmutableMap.copyOf(stateCandidate);
        }
        stateCandidate = null;
    }

    private Map<K, E> getState() {
        if (stateCandidate != null) {
            return stateCandidate;
        }
        return state;
    }

    private Map<K, E> getStateToModify() {
        if (stateCandidate == null) {
            stateCandidate = new HashMap<>();
            for (E entityToCopy : state.values()) {
                E clone = cloneEntity(entityToCopy);
                stateCandidate.put(clone.getKey(), clone);
            }
        }
        return stateCandidate;
    }
}
