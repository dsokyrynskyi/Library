package com.softserveinc.dsoky.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class AuthorDTO {
    @JsonProperty("id")
    private long id;

    @NotEmpty
    @NotNull
    @Size(max = 250)
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
