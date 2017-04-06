package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Book;
import com.softserveinc.dsoky.exceptions.NoSuchBookException;

import java.util.List;

public interface BookDAO {
    List<Book> getAll();
    List<Book> getByAuthor(long authorId);
    Book get(long id) throws NoSuchBookException;
    Book getByName(String name) throws NoSuchBookException;
    /*void save(Book book);*/
    void update(Book book);
    void remove(long id);
}
