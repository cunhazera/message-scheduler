package org.cunha.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<CustomException> {

    @Override
    public Response toResponse(CustomException e) {
        return Response.status(BAD_REQUEST).entity(e.getMessage()).build();
    }

}