package com.softserveinc.dsoky.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Author {
    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String country;
    @JsonProperty
    private LocalDate date;
}
