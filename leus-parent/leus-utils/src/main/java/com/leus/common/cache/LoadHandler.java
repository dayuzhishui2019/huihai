package com.leus.common.cache;

public interface LoadHandler<K, V> {

    V load(K key, V oldValue);

    default V load(K key) {
        return load(key, null);
    }

}
