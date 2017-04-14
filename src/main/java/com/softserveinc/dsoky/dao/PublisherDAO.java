package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Publisher;

import java.util.List;

public interface PublisherDAO {
    List<Publisher> getAll();
    Publisher get(long id);
    void save(Publisher publisher);
    void remove(long id);
    void update(Publisher publisher);
    Publisher getByBook(long id);
}
