package com.softserveinc.dsoky.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.AssertTrue;
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
    @Size(max = 100)
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

    @AssertTrue(message = "bad date format")
    private boolean isDateFormatCorrect(){
        return publishDate==null || publishDate.matches("^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$");
    }
}
