package net.frey;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import net.frey.entity.ResponseModel;
import net.frey.exception.MissingGameException;

@Provider
public class MissingGameExceptionHandler implements ExceptionMapper<MissingGameException> {
    @Override
    public Response toResponse(MissingGameException exception) {
        return Response.status(NOT_FOUND)
                .entity(new ResponseModel(exception.getMessage(), NOT_FOUND.getStatusCode()))
                .build();
    }
}
