package biz.princeps.lib.manager;

import java.util.*;

/**
 * Created by spatium on 17.07.17.
 */
public abstract class ListedManager<T> extends Manager implements IListed<T> {

    protected List<T> elements;

    public ListedManager() {
        elements = new ArrayList<>();
    }

    @Override
    public void add(T t) {
        elements.add(t);
    }

    @Override
    public void remove(T t) {
        elements.remove(t);
    }

    @Override
    public T get(int index) {
        return elements.get(index);
    }

    @Override
    public List<T> getAll() {
        return elements;
    }

    @Override
    public long size() {
        return elements.size();
    }
}
