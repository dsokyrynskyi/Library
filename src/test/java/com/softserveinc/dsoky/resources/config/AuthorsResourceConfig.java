package com.softserveinc.dsoky.resources.config;

import com.softserveinc.dsoky.resources.AuthorsResource;
import com.softserveinc.dsoky.service.AuthorService;
import io.dropwizard.jersey.DropwizardResourceConfig;

import javax.ws.rs.core.UriBuilder;

public class AuthorsResourceConfig extends DropwizardResourceConfig{

    private AuthorsResource resource;

    public AuthorsResourceConfig(AuthorService resourceService) {
        resource = new AuthorsResource(resourceService);
        resource.setUri(UriBuilder.fromPath("http://localhost:8080/v1/").build());
        register(resource);
    }
}
