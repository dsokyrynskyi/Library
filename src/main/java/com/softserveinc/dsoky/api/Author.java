package com.softserveinc.dsoky.api;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Author {
    private long id;
    private String name;
    private String country;
    private LocalDate date;
}
