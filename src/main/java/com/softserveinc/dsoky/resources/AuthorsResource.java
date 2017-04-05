package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.api.Author;
import com.softserveinc.dsoky.dao.AuthorDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 GET     /authors/
 POST    /authors/
 PUT     /authors/
 GET     /authors/title/{name}
 DELETE  /authors/{id}
 GET     /authors/{id}
 */

@Component
@Path("/authors/")
public class AuthorsResource {

    private final AuthorDAO authorDAO;

    @Autowired
    public AuthorsResource(AuthorDAO authorDAO) {
        this.authorDAO = authorDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Author> fetchAll() {
        return authorDAO.getAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Author fetchAuthor(@PathParam("id") long id) {
        return authorDAO.get(id);
    }

    @GET
    @Path("title/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Author> fetchByName(@PathParam("name") String name) {
        return authorDAO.getByBook(name);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void saveBook(Author author) {
        authorDAO.save(author);
    }

    @DELETE
    @Path("{id}")
    public void removeBook(@PathParam("id") long id) {
        authorDAO.remove(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void updateBook(Author author) {
        authorDAO.update(author);
    }
}
