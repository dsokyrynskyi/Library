package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.service.PublisherService;
import io.dropwizard.jersey.DropwizardResourceConfig;

import javax.ws.rs.core.UriBuilder;

public class PublishersResourceConfig extends DropwizardResourceConfig{

    private PublisherResource resource;

    PublishersResourceConfig(PublisherService service){
            resource = new PublisherResource(service);
            resource.setUri(UriBuilder.fromPath("http://localhost:8080/v1/").build());
            register(resource);
    }
}
