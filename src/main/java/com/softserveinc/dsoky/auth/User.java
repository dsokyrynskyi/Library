package com.softserveinc.dsoky.auth;

import java.security.Principal;
import java.util.Set;

public class User implements Principal{
    private String name;
    private Set<String> roles;

    public User(String username, Set<String> roles) {
        this.name = username;
        this.roles = roles;
    }

    @Override
    public String getName() {
        return name;
    }


    public Set<String> getRoles() {
        return roles;
    }
}
