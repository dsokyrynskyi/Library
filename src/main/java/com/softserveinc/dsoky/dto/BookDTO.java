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
}
