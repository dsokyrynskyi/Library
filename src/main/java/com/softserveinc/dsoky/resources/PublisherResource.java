package com.softserveinc.dsoky.resources;

import com.softserveinc.dsoky.dto.PublisherDTO;
import com.softserveinc.dsoky.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 GET     /publishers/
 GET     /publishers/{id}
 GET     /publishers/title?name={name}
 POST    /publishers/
 PUT     /publishers/{id}
 DELETE  /publishers/{id}
 * */

@Component
@Path("/publishers/")
public class PublisherResource {

    private final PublisherService publisherService;

    @Autowired
    public PublisherResource(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PublisherDTO> fetchAll() {
        return publisherService.getAllDTOs();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public PublisherDTO fetchPublisher(@PathParam("id") long id) {
        return publisherService.getDTO(id);
    }

    @GET
    @Path("title")
    @Produces(MediaType.APPLICATION_JSON)
    public PublisherDTO fetchByName(@QueryParam("name") String name) {
        return publisherService.getDTOByBook(name);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void savePublisher(PublisherDTO publisherDTO){
        publisherService.save(publisherDTO);
    }

    @DELETE
    @Path("{id}")
    public void removePublisher(@PathParam("id") long id) {
        publisherService.remove(id);
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void updatePublisher(PublisherDTO publisherDTO){ publisherService.update(publisherDTO);
    }
}
