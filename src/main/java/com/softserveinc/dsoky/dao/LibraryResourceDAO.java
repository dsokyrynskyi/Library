package com.softserveinc.dsoky.dao;

import java.util.List;

public interface LibraryResourceDAO <T>{
    List<T> getAll();
    T get(long id);
    void save(T entity);
    void remove(long id);
    void update(T entity);
}
