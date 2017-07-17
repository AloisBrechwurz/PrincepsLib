package biz.princeps.lib.manager;

import java.util.List;

/**
 * Created by spatium on 17.07.17.
 */
public interface IListed<T> {

    void add(T t);

    void remove(T t);

    T get(int index);

    long size();

    List<T> getAll();
}
