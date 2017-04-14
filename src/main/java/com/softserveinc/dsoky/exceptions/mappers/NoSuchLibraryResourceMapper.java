package com.softserveinc.dsoky.exceptions.mappers;

import com.softserveinc.dsoky.exceptions.NoSuchLibraryResourceException;
import io.dropwizard.jersey.errors.ErrorMessage;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class NoSuchLibraryResourceMapper implements ExceptionMapper<NoSuchLibraryResourceException>{

    @Override
    public Response toResponse(NoSuchLibraryResourceException exception) {
        return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), exception.getMessage()))
                .build();
    }
}
