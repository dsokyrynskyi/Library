package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Author;
import com.softserveinc.dsoky.api.Book;

import java.util.Collection;

public interface BookDAO {
    Collection<Book> getAll();
    Collection<Book> getByAuthor(String author);
    Book get(long id);
    Book getByName(String name);
    void save(Book book);
    void update(Book book);
    void remove(long id);
}
