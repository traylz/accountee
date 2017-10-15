package com.gsobko.act.db;

import java.util.Collection;
import java.util.Optional;

public interface Dao<K, E extends Entity<K>> {

    Optional<E> find(K byKey);

    //May be useful when using Read Committed transaction isolation level
    default Optional<E> findForUpdate(K byKey) {
        return find(byKey);
    }

    /**
     * @param entity entity having non-null key
     * @return updated entity
     */
    E update(E entity);

    /**
     * Null key may be allowed here depending on database setup. If entity has sequence on key, then null key is allowed.
     * Otherwise DaoException will be thrown
     * @param entity
     * @return
     */
    E create(E entity);


    void delete(K byKey);

    Collection<E> getAll();
}
