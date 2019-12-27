package com.leus.common.base.handler;

import com.leus.common.vo.KeyValuePair;

/**
 * @author dyh
 */
public interface KeyValueHandler<K, V> {
    KeyValuePair<K, V> handler(K key, V value);
}
