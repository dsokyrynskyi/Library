package com.softserveinc.dsoky.dao;

import com.softserveinc.dsoky.api.Author;
import com.softserveinc.dsoky.api.Book;
import com.softserveinc.dsoky.api.Genre;
import com.softserveinc.dsoky.api.Publisher;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
        book.setIsbn("111111111111");
        Genre genre = new Genre();
        genre.setName("horror");
        Publisher publisher = new Publisher();
        publisher.setName("EKSMO");
        List<Author> authors = new ArrayList<>();
        Author a1 = new Author();
        Author a2 = new Author();
        a1.setId(1);
        a1.setCountry("Finland");
        a1.setDate(LocalDate.now());
        a1.setName("Kolya");
        a2.setId(2);
        a2.setCountry("Brazil");
        a2.setDate(LocalDate.now());
        a2.setName("Olya");
        authors.addAll(Arrays.asList(a1, a2));
        book.setAuthors(authors);
        book.setGenre(genre);
        book.setPublisher(publisher);
        return book;
    }

    @Override
    public Book getByName(String name) {
        Book book = new Book();
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
