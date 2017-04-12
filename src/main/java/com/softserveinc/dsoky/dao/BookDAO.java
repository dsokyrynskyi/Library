package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Book;
import com.softserveinc.dsoky.exceptions.NoSuchLibraryResourceException;

import java.util.List;

public interface BookDAO {
    List<Book> getAll();
    List<Book> getByAuthor(long authorId);
    List<Book> getByPublisher(long id);
    Book get(long id) throws NoSuchLibraryResourceException;
    Book getByName(String name);
    void remove(long id);
    void save(Book book);
    void update(Book book);
}
