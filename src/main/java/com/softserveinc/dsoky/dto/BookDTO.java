package com.softserveinc.dsoky.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

@Data
public class BookDTO{
    @JsonProperty("id")
    private long id;

    @NotEmpty
    @NotNull
    @Size(max = 250)
    @JsonProperty("name")
    private String name;

    @NotEmpty
    @NotNull
    @Size(max = 150)
    @JsonProperty("isbn")
    private String isbn;

    @JsonProperty("publishDate")
    private String publishDate;

    @JsonProperty("genre")
    private String genre;

    public BookDTO() {
    }

    public BookDTO(String name) {
        this.name = name;
    }

    public BookDTO(long id, String name, String isbn, String publishDate, String genre) {
        this.id = id;
        this.name = name;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.genre = genre;
    }
}
