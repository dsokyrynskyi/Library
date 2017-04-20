package com.softserveinc.dsoky.exceptions.mappers;

import com.softserveinc.dsoky.exceptions.DeleteResourceException;
import io.dropwizard.jersey.errors.ErrorMessage;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class DeleteResourceMapper implements ExceptionMapper<DeleteResourceException> {

    @Override
    public Response toResponse(DeleteResourceException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(), exception.getMessage()))
                .build();
    }
}
