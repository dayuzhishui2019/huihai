package com.dayu.management.core;

import com.leus.common.util.BeanUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Query implements Map<String, Object> {


    public static Query create(int size, int start) {
        return new Query().size(size).start(start);
    }

    public static Query create() {
        return new Query();
    }



    private Map<String, Object> values;

    private Query() {
        this.values = new HashMap<>();
    }

    public Query start(int start) {
        values.put("start", start);
        return this;
    }

    public Query size(int size) {
        values.put("size", size);
        return this;
    }

    public Query set(String k, Object v) {
        values.put(k, v);
        return this;
    }

    public Query with(Object bean) {
        values.putAll(BeanUtil.toMap(bean));
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
    public boolean containsKey(Object o) {
        return values.containsKey(o);
    }

    @Override
    public boolean containsValue(Object o) {
        return values.containsValue(o);
    }

    @Override
    public Object get(Object o) {
        return values.get(o);
    }

    @Override
    public Object put(String s, Object o) {
        return values.put(s, o);
    }

    @Override
    public Object remove(Object o) {
        return values.remove(o);
    }

    @Override
    public void putAll(Map<? extends String, ?> map) {
        values.putAll(map);
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public Set<String> keySet() {
        return values.keySet();
    }

    @Override
    public Collection<Object> values() {
        return values.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return values.entrySet();
    }

    @Override
    public String toString() {
        return "Query" + values;
    }
}
