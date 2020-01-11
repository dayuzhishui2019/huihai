package com.leus.common.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.*;

public class AsyncCache<K, V> implements LoadingCache<K, V>, MethodInterceptor {

    private LoadingCache<K, V> loadingCache;

    private LoadHandler<K, V> loadHandler;

    private ListeningExecutorService listeningService;


    private AsyncCache<K, V> create(CacheBuilder builder, long refreshSecond, LoadHandler<K, V> loadHandler, ExecutorService service) {
        this.listeningService = MoreExecutors.listeningDecorator(service);
        this.loadHandler = loadHandler;
        loadingCache = builder.refreshAfterWrite(refreshSecond, TimeUnit.SECONDS).build(new CacheLoader<K, V>() {
            @Override
            public V load(K k) throws Exception {
                return loadHandler.load(k);
            }

            @Override
            public ListenableFuture<V> reload(K key, V oldValue) throws Exception {
                return listeningService.submit(() -> loadHandler.load(key, oldValue));
            }
        });
        return this;
    }

    public static <K, V> LoadingCache<K, V> createDefault(LoadHandler<K, V> loadHandler) {
        return proxy(new AsyncCache<K, V>()
                .create(CacheBuilder.newBuilder()
                                .expireAfterWrite(30, TimeUnit.MINUTES),
                        10,
                        loadHandler,
                        Executors.newSingleThreadExecutor()));
    }

    private static <K, V> LoadingCache<K, V> proxy(AsyncCache<K, V> proxyObject) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(LoadingCache.class);
        enhancer.setCallback(proxyObject);
        return (LoadingCache<K, V>) enhancer.create();
    }


    @Nullable
    @Override
    public V getIfPresent(Object o) {
        return null;
    }

    @Override
    public V get(K k, Callable<? extends V> callable) throws ExecutionException {
        return null;
    }

    @Override
    public ImmutableMap<K, V> getAllPresent(Iterable<?> iterable) {
        return null;
    }

    @Override
    public void put(K k, V v) {

    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {

    }

    @Override
    public void invalidate(Object o) {

    }

    @Override
    public void invalidateAll(Iterable<?> iterable) {

    }

    @Override
    public void invalidateAll() {

    }

    @Override
    public long size() {
        return 0;
    }

    @Override
    public CacheStats stats() {
        return null;
    }

    @Override
    public V get(K k) throws ExecutionException {
        return null;
    }

    @Override
    public V getUnchecked(K k) {
        return null;
    }

    @Override
    public ImmutableMap<K, V> getAll(Iterable<? extends K> iterable) throws ExecutionException {
        return null;
    }

    @Override
    public V apply(K k) {
        return null;
    }

    @Override
    public void refresh(K k) {

    }

    @Override
    public ConcurrentMap<K, V> asMap() {
        return null;
    }

    @Override
    public void cleanUp() {

    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return method.invoke(loadingCache, objects);
    }

}
