package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.api.Publisher;
import com.softserveinc.dsoky.dao.PublisherDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 GET     /publishers/
 POST    /publishers/
 PUT     /publishers/
 GET     /publishers/title/{name}
 DELETE  /publishers/{id}
 GET     /publishers/{id}
 * */

@Component
@Path("/publishers/")
public class PublisherResource {

    private final PublisherDAO publisherDAO;

    @Autowired
    public PublisherResource(PublisherDAO publisherDAO) {
        this.publisherDAO = publisherDAO;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Publisher> fetchAll() {
        return publisherDAO.getAll();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Publisher fetchPublisher(@PathParam("id") long id) {
        return publisherDAO.get(id);
    }

    @GET
    @Path("title/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Publisher fetchByName(@PathParam("name") String name) {
        return publisherDAO.getByBook(name);
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void savePublisher(Publisher publisher){
        publisherDAO.save(publisher);
    }

    @DELETE
    @Path("{id}")
    public void removePublisher(@PathParam("id") long id) {
        publisherDAO.remove(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void updatePublisher(Publisher publisher){ publisherDAO.update(publisher);
    }
}
