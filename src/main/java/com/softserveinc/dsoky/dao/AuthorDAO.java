package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Author;

import java.util.List;

public interface AuthorDAO {
    List<Author> getAll();
    List<Author> getByBook(long id);
    Author get(long id);
    void save(Author author);
    void remove(long id);
    void update(Author author);
}
