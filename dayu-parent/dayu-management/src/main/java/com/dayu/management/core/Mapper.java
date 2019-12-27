package com.dayu.management.core;

import java.util.List;

public interface Mapper<T> {

    int insert(List<T> list);

    int update(List<T> list);

    int delete(List<String> id);

    T get(String id);

    List<T> select(Query query);
}
