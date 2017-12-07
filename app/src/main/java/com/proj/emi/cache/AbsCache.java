package com.proj.emi.cache;

interface AbsCache<V> {

    V get(String key);

    void put(String key, V value);

    void remove(String key);
}
