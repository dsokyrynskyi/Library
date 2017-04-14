package com.softserveinc.dsoky.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BookDTO{
    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("isbn")
    private String isbn;
    @JsonProperty("publishDate")
    private String publishDate;
    @JsonProperty("genre")
    private String genre;

    public BookDTO() {
    }

    public BookDTO(long id, String name, String isbn, String publishDate, String genre) {
        this.id = id;
        this.name = name;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.genre = genre;
    }
}
