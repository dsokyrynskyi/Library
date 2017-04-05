package com.softserveinc.dsoky.api;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Author {
    private long id;
    private String name;
    private String country;
    private LocalDate date;

    public Author() {
    }

    public Author(long id) {
        this.id = id;
    }

    public Author(long id, String name, String country, LocalDate date) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.date = date;
    }
}
