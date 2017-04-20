package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Author;

import java.util.List;

public interface AuthorDAO extends LibraryResourceDAO<Author>{
    List<Author> getByBook(long id);
    public void insertAuthorForBook(long bookId, long authorId);
}
