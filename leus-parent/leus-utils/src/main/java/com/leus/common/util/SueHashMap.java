package com.leus.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sue
 * @date 2017/10/19 11:40
 */
public class SueHashMap<K, V> extends HashMap<K, V> implements GetValue<K>
{

    public static <K, V> SueHashMap<K, V> newSueHashMap()
    {
        return new SueHashMap<>();
    }

    public static <K, V> SueHashMap<K, V> newSueHashMap(Map<K, V> map)
    {
        SueHashMap<K, V> sueHashMap = new SueHashMap<>();
        sueHashMap.putAll(map);
        return sueHashMap;
    }

    public SueHashMap()
    {

    }

    @Override
    public Integer getInteger(K key)
    {
        return isInstanceThenGet(get(key), Integer.class);
    }

    @Override
    public String getString(K key)
    {
        return isInstanceThenGet(get(key), String.class);
    }

    @Override
    public Long getLong(K key)
    {
        return isInstanceThenGet(get(key), Long.class);
    }

    @Override
    public Double getDouble(K key)
    {
        return isInstanceThenGet(get(key), Double.class);
    }

    @Override
    public Float getFloat(K key)
    {
        return isInstanceThenGet(get(key), Float.class);
    }

    private <E> E isInstanceThenGet(Object v, Class<E> clz)
    {
        if (clz.isInstance(v))
        {
            return clz.cast(v);
        }
        return null;
    }

}
