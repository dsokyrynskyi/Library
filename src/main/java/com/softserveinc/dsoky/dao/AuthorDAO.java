package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Author;

import java.util.List;

public interface AuthorDAO {
    List<Author> getAll();
    List<Author> getByBook();
    Author get(long id);
}
