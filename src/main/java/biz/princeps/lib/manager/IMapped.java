package biz.princeps.lib.manager;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Created by spatium on 17.07.17.
 */
public interface IMapped<K, V> {


    void add(K key, V value);

    void addAll(Map<K, V> map);

    void remove(K key);

    V get(K key);

    Set<K> keySet();

    Collection<V> values();

    long size();


}
