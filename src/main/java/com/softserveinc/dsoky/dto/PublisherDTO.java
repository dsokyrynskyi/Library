package com.softserveinc.dsoky.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PublisherDTO {
    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("country")
    private String country;

    public PublisherDTO(long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public PublisherDTO() {
    }
}
