package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Publisher;

import java.util.List;

public interface PublisherDAO {
    List<Publisher> getAll();
    Publisher get(long id);
    Publisher getByBook(String name);
    void save(Publisher publisher);
    void remove(long id);
    void update(Publisher publisher);
}
