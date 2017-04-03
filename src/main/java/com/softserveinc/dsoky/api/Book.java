package com.softserveinc.dsoky.api;

import lombok.Data;

import java.util.List;

@Data
public class Book {
    private long id;
    private String name;
    private String isbn;
    private Genre genre;
    private Publisher publisher;
    private List<Author> authors;
}
