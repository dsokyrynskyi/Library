package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.dto.AuthorDTO;
import com.softserveinc.dsoky.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

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
    public void saveAuthor(@Valid AuthorDTO authorDTO) {
        authorService.save(authorDTO);
    }

    @POST
    @Path("/books/{bId}/authors/{aId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void saveAuthorForBook(@PathParam("bId") long bId, @PathParam("aId") long aId) {
        authorService.insertForBook(bId, aId);
    }


    @DELETE
    @Path("/authors/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void removeAuthor(@PathParam("id") long id) {
        authorService.remove(id);
    }

    @DELETE
    @Path("authors/{aId}/books/{bId}/")
    @Produces(MediaType.APPLICATION_JSON)
    public void removeRelation(@PathParam("bId") long bId, @PathParam("aId") long aId) {
        authorService.removeRelation(bId, aId);
    }

    @PUT
    @Path("/authors/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateAuthor(@PathParam("id") long id, @Valid AuthorDTO authorDTO) {
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
