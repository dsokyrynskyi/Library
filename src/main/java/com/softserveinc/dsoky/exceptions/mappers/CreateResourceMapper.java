package com.softserveinc.dsoky.exceptions.mappers;

import com.softserveinc.dsoky.exceptions.CreateResourceException;
import io.dropwizard.jersey.errors.ErrorMessage;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class CreateResourceMapper  implements ExceptionMapper<CreateResourceException> {

    @Override
    public Response toResponse(CreateResourceException exception) {
        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ErrorMessage(Response.Status.BAD_REQUEST.getStatusCode(), exception.getMessage()))
                .build();
    }
}
