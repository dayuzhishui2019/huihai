package com.leus.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;

import java.util.*;


public final class HierarchyMap extends AbstractMap<String, Object> {
    private Map<String, Object> target;


    private HierarchyMap(Map<String, Object> map) {
        this.target = map;
    }


    public static HierarchyMap decorate(Map<String, Object> map) {
        return new HierarchyMap(map);
    }

    public static HierarchyMap create() {
        return new HierarchyMap(Maps.newLinkedHashMap());
    }


    public static HierarchyMap decorate(String json) {
        Map<String, Object> map = JSON.parseObject(json, new TypeReference<Map<String, Object>>() {
        });
        return new HierarchyMap(map);
    }


    @Override
    public int size() {
        return target.size();
    }

    @Override
    public boolean isEmpty() {
        return target.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public Object get(Object key) {
        Queue<String> keys = buildKeys(Objects.toString(key));
        Map<String, Object> values = getTarget(target, keys, false);
        if (values == null) {
            return null;
        }
        return values.get(keys.poll());
    }

    @Override
    public Object put(String key, Object value) {
        Queue<String> keys = buildKeys(key);
        Map<String, Object> values = getTarget(target, keys, true);
        Preconditions.checkNotNull(values, "namespace [" + Joiner.on(".").join(keys) + "] not exist");
        values.put(keys.poll(), value);
        return value;
    }

    @Override
    public Object remove(Object key) {
        throw new UnsupportedOperationException("NameSpace 不支持Remove操作");
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        throw new UnsupportedOperationException("NameSpace 不支持putAll操作");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("NameSpace 不支持clear操作");
    }

    @Override
    public Set<String> keySet() {
        return target.keySet();
    }

    @Override
    public Collection<Object> values() {
        return target.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return target.entrySet();
    }

    public <T> T get(String key, Class<T> tClass) {
        Object o = get(key);
        if (o == null) {
            return null;
        }
        Preconditions.checkState(o.getClass().equals(tClass), "Type error,the value is : " + o);
        return (T) o;
    }

    private Queue<String> buildKeys(String key) {
        return Queues.newLinkedBlockingDeque(Splitter.on(".").trimResults().split(key));
    }

    private void namespace(String key) {
        Queue<String> keys = buildKeys(key);
        Map<String, Object> map = getTarget(target, keys, true);
        String lastKey = keys.poll();
        if (!map.containsKey(lastKey)) {
            Map<String, Object> lastMap = Maps.newHashMap();
            map.put(lastKey, lastMap);
        }
    }


    private Map<String, Object> getTarget(Map<String, Object> map, Queue<String> keys, boolean autoBuild) {
        if (keys.size() == 1) {
            return map;
        }
        String key = keys.poll();
        Object value = map.get(key);
        if (value == null && !autoBuild) {
            return null;
        } else {
            if (autoBuild && value == null) {
                value = Maps.newHashMap();
                map.put(key, value);
            }
            Preconditions.checkState(value instanceof Map, "value [" + value + "] type is not Map");
            if (keys.size() == 1) {
                return (Map<String, Object>) value;
            } else {
                return getTarget((Map<String, Object>) value, keys, autoBuild);
            }
        }
    }
}
