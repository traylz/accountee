package com.gsobko.act.rest;

import com.gsobko.act.ActUserException;
import com.gsobko.act.db.DaoExcepton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class RuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {
    private static final Logger logger = LoggerFactory.getLogger(RuntimeExceptionMapper.class);
    @Override
    public Response toResponse(RuntimeException exception) {
        if (exception instanceof IllegalArgumentException
                || exception instanceof ActUserException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Bad request : " + exception.getMessage())
                    .build();
        } else if (exception instanceof WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        } else {
            String reference = getReferenceForException(exception);
            logger.error("Internal server error occurred. Ref = " + reference, exception);
            return Response
                    .serverError()
                    .entity("Internal server error occurred. Please contact support with reference " + reference)
                    .build();
        }
    }

    private String getReferenceForException(RuntimeException exception) {
        // Used for Internal server error - so that user can reference support and they can find stacktrace in logs
        return String.valueOf(Math.abs((long)System.identityHashCode(exception)));
    }
}
