package com.softserveinc.dsoky.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RichBookDTO extends BookDTO{
    @JsonProperty("publisher")
    private long publisher;
    @JsonProperty("authors")
    private List<Long> authors = new ArrayList<>();
}
