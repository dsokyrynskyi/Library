package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Author;
import com.softserveinc.dsoky.api.Book;

import java.util.Collection;

public class BookDAOImpl implements BookDAO{

    @Override
    public Collection<Book> getAll() {
        return null;
    }

    @Override
    public Collection<Book> getByAuthor(Author author) {
        return null;
    }

    @Override
    public Book get(long id) {
        Book book = new Book();
        book.setId(789);
        book.setName("Scala in Depth");
        book.setIsnb("3223424234234");
        return book;
    }

    @Override
    public Book getByName(String name) {
        Book book = new Book();
        book.setId(12345);
        book.setName("Scala in Action");
        book.setIsnb("1636414981");
        return book;
    }

    @Override
    public void save(Book book) {
        System.out.printf("save");
    }

    @Override
    public void update(Book book) {
        System.out.printf("update");
    }

    @Override
    public void remove(long id) {
        System.out.printf("remove");
    }
}
