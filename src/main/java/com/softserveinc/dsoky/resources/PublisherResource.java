package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.dto.PublisherDTO;
import com.softserveinc.dsoky.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 GET     /v1/publishers/
 POST    /v1/publishers/
 DELETE  /v1/publishers/{id}
 GET     /v1/publishers/{id}
 PUT     /v1/publishers/{id}
 GET     /v1/books/{id}/publisher
 * */

@Component
@Path("v1/")
public class PublisherResource {

    private final PublisherService publisherService;

    @Autowired
    public PublisherResource(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GET
    @Path("publishers/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchAll() {
        return Response.ok(publisherService.getAllDTOs())
                .link("http://localhost:8080/v1/authors", "authors")
                .link("http://localhost:8080/v1/books", "books")
                .build();
    }

    @GET
    @Path("publishers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchPublisher(@PathParam("id") long id) {
        return Response.ok(publisherService.getDTO(id))
                .link("http://localhost:8080/v1/publishers/" + id + "/books", "books")
                .build();
    }

    @POST
    @Path("publishers/")
    @Consumes(MediaType.APPLICATION_JSON)
    public void savePublisher(PublisherDTO publisherDTO) {
        publisherService.save(publisherDTO);
    }

    @DELETE
    @Path("publishers/{id}")
    public void removePublisher(@PathParam("id") long id) {
        publisherService.remove(id);
    }

    @PUT
    @Path("publishers/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updatePublisher(PublisherDTO publisherDTO) {
        publisherService.update(publisherDTO);
    }

    @GET
    @Path("books/{id}/publisher")
    @Produces(MediaType.APPLICATION_JSON)
    public PublisherDTO getPublisherOfBook(@PathParam("id") long id){
        return publisherService.getDTOByBook(id);
    }
}
