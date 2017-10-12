package com.gsobko.act.db;

/**
 *
 * @param <K> Key (id) type for entity
 */
public interface Entity<K> extends Cloneable {
    K getKey();
    Entity<K> clone();
}
