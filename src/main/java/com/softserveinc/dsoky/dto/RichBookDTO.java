package com.softserveinc.dsoky.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RichBookDTO {
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

    @JsonProperty("publisher")
    private long publisher;
    @JsonProperty("authors")
    private List<Long> authors = new ArrayList<>();
}
