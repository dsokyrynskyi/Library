package com.softserveinc.dsoky.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Book {
    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String isnb;
    @JsonProperty
    private Author author;
    @JsonProperty
    private Genre genre;
    @JsonProperty
    private Publisher publisher;
}
