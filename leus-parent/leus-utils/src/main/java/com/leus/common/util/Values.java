package com.leus.common.util;

import com.google.common.collect.Maps;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;


public final class Values {

    private Values() {
    }

    public static <K> Value<K> set(K k, Object v) {
        return new Value<K>().set(k, v);
    }

    public static Value<String> use(HierarchyMap map) {
        return new Value<>(map);
    }

    public static <K> Value<K> use(Map<K, Object> map) {
        return new Value<>(map);
    }

    public final static class Value<K> extends AbstractMap<K, Object> {

        private Map<K, Object> values;

        private Value() {
            values = Maps.newLinkedHashMap();
        }

        private Value(Map<K, Object> map) {
            values = map;
        }

        public Value set(K key, Object value) {
            put(key, value);
            return this;
        }

        @Override
        public int size() {
            return values.size();
        }

        @Override
        public boolean isEmpty() {
            return values.isEmpty();
        }

        @Override
        public boolean containsKey(Object key) {
            return values.containsKey(key);
        }

        @Override
        public boolean containsValue(Object value) {
            return values.containsValue(value);
        }

        @Override
        public Object get(Object key) {
            return values.get(key);
        }

        @Override
        public Object put(K key, Object value) {
            return values.put(key, value);
        }

        @Override
        public Object remove(Object key) {
            return values.remove(key);
        }

        @Override
        public void putAll(Map<? extends K, ?> m) {
            values.putAll(m);
        }

        @Override
        public void clear() {
            values.clear();
        }

        @Override
        public Set<K> keySet() {
            return values.keySet();
        }

        @Override
        public Collection<Object> values() {
            return values.values();
        }

        @Override
        public Set<Map.Entry<K, Object>> entrySet() {
            return values.entrySet();
        }


    }
}
