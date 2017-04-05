package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Book;

import java.util.List;

public interface BookDAO {
    List<Book> getAll();
    List<Book> getByAuthor(String author);
    Book get(long id);
    Book getByName(String name);
    void save(Book book);
    void update(Book book);
    void remove(long id);
}
