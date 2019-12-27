package com.leus.common.util;

/**
 * @author sue
 * @date 2017/10/19 11:45
 */
public interface GetValue<K>
{
    Integer getInteger(K key);

    String getString(K key);

    Long getLong(K key);

    Double getDouble(K key);

    Float getFloat(K key);

}
