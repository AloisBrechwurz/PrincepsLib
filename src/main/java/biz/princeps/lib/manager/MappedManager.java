package biz.princeps.lib.manager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by spatium on 17.07.17.
 */
public abstract class MappedManager<K, V> extends Manager implements IMapped<K, V> {

    protected Map<K, V> elements;

    public MappedManager() {
        elements = new HashMap<>();
    }

    @Override
    public void add(K key, V value) {
        elements.put(key, value);
    }

    @Override
    public void remove(K key) {
        elements.remove(key);
    }

    @Override
    public V get(K key) {
        return elements.get(key);
    }

    @Override
    public Set<K> keySet() {
        return elements.keySet();
    }

    @Override
    public Collection<V> values() {
        return elements.values();
    }

    @Override
    public long size() {
        return elements.size();
    }
}
