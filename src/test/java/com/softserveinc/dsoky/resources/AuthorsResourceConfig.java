package com.softserveinc.dsoky.resources;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.softserveinc.dsoky.service.AuthorService;
import io.dropwizard.jersey.DropwizardResourceConfig;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

class AuthorsResourceConfig extends DropwizardResourceConfig{

    private AuthorsResource resource;

    AuthorsResourceConfig(AuthorService resourceService) {
        resource = new AuthorsResource(resourceService);
        resource.setUri(UriBuilder.fromPath("http://localhost:8080/v1/").build());
        register(resource);

        JacksonJsonProvider provider = new JacksonJsonProvider();
        provider.addUntouchable(Response.class);
        this.registerInstances(provider);

    }
}
