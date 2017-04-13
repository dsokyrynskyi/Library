package com.softserveinc.dsoky.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AuthorDTO {
    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("country")
    private String country;
    @JsonProperty("birthDate")
    private String date;

    public AuthorDTO() {
    }

    public AuthorDTO(long id, String name, String country, String date) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.date = date;
    }
}
