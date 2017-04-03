package com.softserveinc.dsoky.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Genre {
    @JsonProperty
    private long id;
    @JsonProperty
    private String name;
}
