package com.softserveinc.dsoky.api;

import lombok.Data;

@Data
public class Publisher {
    private long id;
    private String name;
    private String country;

    public Publisher(long id) {
        this.id=id;
    }

    public Publisher(long id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public Publisher(String name, String country) {
        this.name = name;
        this.country = country;
    }
}
