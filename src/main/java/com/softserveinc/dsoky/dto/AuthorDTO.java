package com.softserveinc.dsoky.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.*;
import java.text.Format;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
    @Size(max = 100)
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

    @AssertTrue(message = "bad date format")
    private boolean isDateFormatCorrect(){
        return date==null || date.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$");
    }
}
