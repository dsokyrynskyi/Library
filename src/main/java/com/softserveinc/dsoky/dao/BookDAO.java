package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Book;
import com.softserveinc.dsoky.exceptions.NoSuchLibraryResourceException;

import java.util.List;

public interface BookDAO extends LibraryResourceDAO<Book>{
    List<Book> getByAuthor(long authorId);
    List<Book> getByPublisher(long id);
    Book getByName(String name);
}
