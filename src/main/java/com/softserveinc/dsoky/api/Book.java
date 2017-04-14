package com.softserveinc.dsoky.api;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class Book {
    private long id;
    private String name;
    private String isbn;
    private LocalDate publishDate;
    private String genre;
    private Publisher publisher;
    private List<Author> authors = new ArrayList<>();

    public Book() {
    }

    public Book(long id, String name, String isbn, LocalDate publishDate, String genre) {
        this.id = id;
        this.name = name;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.genre = genre;
    }

    public Book(long id, String name, String isbn, LocalDate publishDate, String genre, Publisher publisher) {
        this.id = id;
        this.name = name;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.genre = genre;
        this.publisher = publisher;
        this.authors = new ArrayList<>();
    }
}
