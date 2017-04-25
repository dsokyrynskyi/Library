package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.dto.AuthorDTO;
import com.softserveinc.dsoky.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static java.lang.String.format;

/**
 GET     /v1/authors/
 POST    /v1/authors/

 DELETE  /v1/authors/{id}
 GET     /v1/authors/{id}
 PUT     /v1/authors/{id}

 GET     /v1/books/{id}/authors
 POST    /v1/books/{bId}/authors/{aId}
 DELETE  /v1/authors/{aId}/books/{bId}
 */

@Component
@Path("v1/")
@RolesAllowed({"USER", "ADMIN"})
public class AuthorsResource{
    private static Logger log = LoggerFactory.getLogger(AuthorsResource.class);

    private URI uri;

    public void setUri(URI uri) {
        this.uri = uri;
    }

    private /*final*/ AuthorService authorService;

    @Autowired
    public AuthorsResource(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostConstruct
    public void init(){
        log.debug("Building URI template for headers from requests to AuthorsResource... ");
        uri = UriBuilder.fromPath("http://{host}:{port}/{version}/")
                .resolveTemplate("host", "localhost")
                .resolveTemplate("port", "8080")
                .resolveTemplate("version", "v1")
                .build();
    }

    @GET
    @Path("/authors/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchAll() {
        log.debug("Fetching all the authors... ");
        URI booksUri = uri.resolve("/books");
        URI publishersUri = uri.resolve("/publishers");
        return Response.ok(authorService.getAllDTOs())
                .link(booksUri, "books")
                .link(publishersUri, "publishers")
                .build();
    }

    @GET
    @Path("/authors/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchAuthor(@PathParam("id") long id) {
        log.debug(format("Fetching the author with  id %d... ", id));
        URI booksUri = uri.resolve("authors/"+id+"/books");
        return Response.ok(authorService.getDTO(id))
                .link(booksUri, "books")
                .build();
    }

    @POST
    @Path("/authors/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void saveAuthor(@Valid AuthorDTO authorDTO) {
        log.debug("Saving the author from UI... ");
        authorService.save(authorDTO);
    }

    @POST
    @Path("/books/{bId}/authors/{aId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void saveAuthorForBook(@PathParam("bId") long bId, @PathParam("aId") long aId) {
        log.debug(format("Binding the author with ID %d to the book with ID %d... ", aId, bId));
        authorService.insertForBook(bId, aId);
    }


    @DELETE
    @Path("/authors/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void removeAuthor(@PathParam("id") long id) {
        log.debug(format("Removing the author #%d... ", id));
        authorService.remove(id);
    }

    @DELETE
    @Path("authors/{aId}/books/{bId}/")
    @Produces(MediaType.APPLICATION_JSON)
    public void removeRelation(@PathParam("bId") long bId, @PathParam("aId") long aId) {
        log.debug(format("Removing the book #%d from the author's #%d list of books ... ", bId, aId));
        authorService.removeRelation(bId, aId);
    }

    @PUT
    @Path("/authors/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateAuthor(@PathParam("id") long id, @Valid AuthorDTO authorDTO) {
        log.debug(format("Updating info about the author #%d... ", id));
        authorDTO.setId(id);
        authorService.update(authorDTO);
    }

    @GET
    @Path("/books/{id}/authors")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AuthorDTO> getAuthorsOfBook(@PathParam("id") long id) {
        log.debug(format("Fetching all the authors for the Book #%d... ", id));
        return authorService.getDTOByBook(id);
    }
}
