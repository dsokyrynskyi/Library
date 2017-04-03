package com.softserveinc.dsoky.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Publisher {
    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
    @JsonProperty
    private String country;
}
