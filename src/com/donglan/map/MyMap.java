package com.donglan.map;

/**
 * @author TAOJIAN
 * @version 1.0
 * @since 2021-01-04 14:01:12
 */
public interface MyMap<K, V> {

    V put(K k, V v);

    V get(K k);

    interface Entry<K, V> {
        K getKey();
        V getValue();
    }
}
