package com.softserveinc.dsoky.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PublisherDTO {
    @JsonProperty("id")
    private long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("country")
    private String country;
}
