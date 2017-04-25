package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.dto.PublisherDTO;
import com.softserveinc.dsoky.service.PublisherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static java.lang.String.format;

/**
 GET     /v1/publishers/
 POST    /v1/publishers/

 DELETE  /v1/publishers/{id}
 GET     /v1/publishers/{id}
 PUT     /v1/publishers/{id}

 GET     /v1/books/{id}/publisher
 POST    /v1/books/{bId}/publisher/{pId}
 PUT     /v1/books/{bId}/publisher/{pId}
 * */

@Component
@Path("v1/")
//@PermitAll
@RolesAllowed({"USER", "ADMIN"})
public class PublisherResource {

    private static Logger log = LoggerFactory.getLogger(PublisherResource.class);
    private URI uri;
    private final PublisherService publisherService;

    public void setUri(URI uri) {
        this.uri = uri;
    }

    @Autowired
    public PublisherResource(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @PostConstruct
    public void init(){
        log.debug("Building URI template for headers from requests to PublisherResource... ");
        uri = UriBuilder.fromPath("http://{host}:{port}/{version}/")
                .resolveTemplate("host", "localhost")
                .resolveTemplate("port", "8080")
                .resolveTemplate("version", "v1")
                .build();
    }

    @GET
    @Path("publishers/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchAll() {
        log.debug("Fetching all the publishers... ");
        URI booksUri = uri.resolve("/books");
        URI authorsUri = uri.resolve("/authors");
        return Response.ok(publisherService.getAllDTOs())
                .link(booksUri, "books")
                .link(authorsUri, "authors")
                .build();
    }

    @GET
    @Path("publishers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchPublisher(@PathParam("id") long id) {
        log.debug(format("Fetching the publisher by id %d... ", id));
        URI booksUri = uri.resolve("publishers/"+id+"/books");
        return Response.ok(publisherService.getDTO(id))
                .link(booksUri, "books")
                .build();
    }

    @POST
    @Path("publishers/")
    @Consumes(MediaType.APPLICATION_JSON)
    public void savePublisher(@Valid PublisherDTO publisherDTO) {
        log.debug("Saving the publisher... ");
        publisherService.save(publisherDTO);
    }

    @DELETE
    @Path("publishers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void removePublisher(@PathParam("id") long id) {
        log.debug(format("Removing the publisher #%d... ", id));
        publisherService.remove(id);
    }

    @PUT
    @Path("publishers/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updatePublisher(@PathParam("id") long id, @Valid PublisherDTO publisherDTO) {
        log.debug(format("Updating the publisher #%d... ", id));
        publisherDTO.setId(id);
        publisherService.update(publisherDTO);
    }

    @GET
    @Path("books/{id}/publisher")
    @Produces(MediaType.APPLICATION_JSON)
    public PublisherDTO getPublisherOfBook(@PathParam("id") long id){
        log.debug(format("Fetching the publisher for Book #%d... ", id));
        return publisherService.getDTOByBook(id);
    }

    @POST
    @Path("books/{bId}/publisher/{pId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void savePublisherForBook(@PathParam("bId") long bId, @PathParam("pId") long pId){
        log.debug(format("Binding the publisher #%d with the book #%d... ", pId, bId));
        publisherService.insertForBook(bId, pId);
    }

    @PUT
    @Path("books/{bId}/publisher/{pId}")
    @Produces(MediaType.APPLICATION_JSON)
    public void updatePublisherForBook(@PathParam("bId") long bId, @PathParam("pId") long pId){
        log.debug(format("Updating the publisher #%d for certain book #%d... ",pId, bId));
        publisherService.insertForBook(bId, pId);
    }
}
