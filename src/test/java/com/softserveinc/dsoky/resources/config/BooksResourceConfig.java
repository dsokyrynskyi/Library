package com.softserveinc.dsoky.resources.config;

import com.softserveinc.dsoky.exceptions.mappers.CreateResourceMapper;
import com.softserveinc.dsoky.exceptions.mappers.NoSuchLibraryResourceMapper;
import com.softserveinc.dsoky.resources.BooksResource;
import com.softserveinc.dsoky.service.BookService;
import io.dropwizard.jersey.DropwizardResourceConfig;

import javax.ws.rs.core.UriBuilder;

public class BooksResourceConfig extends DropwizardResourceConfig{

    private BooksResource resource;

    public BooksResourceConfig(BookService resourceService) {
        resource = new BooksResource(resourceService);
        resource.setUri(UriBuilder.fromPath("http://localhost:8080/v1/").build());
        register(resource);
        register(new NoSuchLibraryResourceMapper());
        register(new CreateResourceMapper());
    }
}
