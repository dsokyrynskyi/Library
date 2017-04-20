package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.dto.AuthorDTO;
import com.softserveinc.dsoky.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

/**
 * GET     /v1/authors/
 * POST    /v1/authors/
 * DELETE  /v1/authors/{id}
 * GET     /v1/authors/{id}
 * PUT     /v1/authors/{id}
 * GET     /v1/books/{id}/authors
 */

@Component
@Path("v1/")
public class AuthorsResource{

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
        URI booksUri = uri.resolve("authors/"+id+"/books");
        return Response.ok(authorService.getDTO(id))
                .link(booksUri, "books")
                .build();
    }

    @POST
    @Path("/authors/")
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveBook(AuthorDTO authorDTO) {
        authorService.save(authorDTO);
    }

    @DELETE
    @Path("/authors/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void removeBook(@PathParam("id") long id) {
        authorService.remove(id);
    }

    @PUT
    @Path("/authors/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateBook(@PathParam("id") long id, AuthorDTO authorDTO) {
        authorDTO.setId(id);
        authorService.update(authorDTO);
    }

    @GET
    @Path("/books/{id}/authors")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AuthorDTO> getAuthorsOfBook(@PathParam("id") long id) {
        return authorService.getDTOByBook(id);
    }
}
