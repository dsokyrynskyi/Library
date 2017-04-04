package com.softserveinc.dsoky.api;

import lombok.Data;

@Data
public class Publisher {
    private long id;
    private String name;
    private String country;

    public Publisher() {
    }

    public Publisher(long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }
}
