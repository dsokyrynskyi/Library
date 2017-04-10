package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.dto.AuthorDTO;
import com.softserveinc.dsoky.dto.BookDTO;
import com.softserveinc.dsoky.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 GET     /authors/
 POST    /authors/
 PUT     /authors/{id}
 GET     /authors/title?name={name}
 DELETE  /authors/{id}
 GET     /authors/{id}
 */

@Component
@Path("v1/")
public class AuthorsResource {

    private final AuthorService  authorService;

    @Autowired
    public AuthorsResource(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GET
    @Path("/authors/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchAll() {
        return Response.ok(authorService.getAllDTOs())
                .link("http://localhost:8080/books", "books")
                .link("http://localhost:8080/publishers", "publishers")
                .build();
    }

    @GET
    @Path("/authors/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response fetchAuthor(@PathParam("id") long id) {
        return Response.ok(authorService.getDTO(id))
                .link("http://localhost:8080/authors/"+id+"/books", "books")
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
    public void removeBook(@PathParam("id") long id) {
        authorService.remove(id);
    }

    @PUT
    @Path("/authors/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateBook(AuthorDTO authorDTO) {
        authorService.update(authorDTO);
    }

    /*@GET
    @Path("title")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AuthorDTO> fetchByName(@QueryParam("name") String name) {
        return authorService.getDTOByBook(name);
    }*/

    @GET
    @Path("/books/{name}/authors")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AuthorDTO> getBooksOfAuthor(@PathParam("name") String name){
        return authorService.getDTOByBook(name);
    }
}
