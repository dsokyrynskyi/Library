package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.dao.AuthorDAO;
import com.softserveinc.dsoky.dto.AuthorDTO;
import com.softserveinc.dsoky.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
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
@Path("/authors/")
public class AuthorsResource {

    private final AuthorService  authorService;

    @Autowired
    public AuthorsResource(AuthorDAO authorDAO, AuthorService authorService) {
        this.authorService = authorService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<AuthorDTO> fetchAll() {
        return authorService.getAllDTOs();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public AuthorDTO fetchAuthor(@PathParam("id") long id) {
        return authorService.getDTO(id);
    }

    @GET
    @Path("title")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AuthorDTO> fetchByName(@QueryParam("name") String name) {
        return authorService.getDTOByBook(name);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveBook(AuthorDTO authorDTO) {
        authorService.save(authorDTO);
    }

    @DELETE
    @Path("{id}")
    public void removeBook(@PathParam("id") long id) {
        authorService.remove(id);
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateBook(AuthorDTO authorDTO) {
        authorService.update(authorDTO);
    }
}
