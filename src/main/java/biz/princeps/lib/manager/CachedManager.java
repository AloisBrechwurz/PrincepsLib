package biz.princeps.lib.manager;

import biz.princeps.lib.storage.DatabaseAPI;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by spatium on 17.07.17.
 */
public abstract class CachedManager<K, V> extends Manager implements IMapped<K, V> {

    protected LoadingCache<K, V> cache;

    public CachedManager(DatabaseAPI api, int maxSize, CacheLoader<K, V> loader) {
        super(api);
        cache = CacheBuilder.newBuilder()
                .maximumSize(maxSize)
                .build(loader);
    }

    @Override
    public void add(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void remove(K key) {
        cache.invalidate(key);
    }

    @Override
    public V get(K key) {
        try {
            return cache.get(key);
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Set<K> keySet() {
        return cache.asMap().keySet();
    }

    @Override
    public Collection<V> values() {
        return cache.asMap().values();
    }

    @Override
    public long size() {
        return cache.size();
    }

    public void refresh(K key) {
        cache.refresh(key);
    }
}
