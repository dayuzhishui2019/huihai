package com.leus.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 键值对
 *
 * @param <K> 键类型
 * @param <V> 值类型
 * @author dyh
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValuePair<K, V> {
    private K key;

    private V value;
}
