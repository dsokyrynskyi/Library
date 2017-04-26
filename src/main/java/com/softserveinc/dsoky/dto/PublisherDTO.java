package com.softserveinc.dsoky.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class PublisherDTO {
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

    public PublisherDTO(long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public PublisherDTO() {
    }
}
