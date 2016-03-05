package de.fhb.jeux.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import org.jboss.resteasy.spi.ReaderException;

// used to map exceptions caused by bad client-provided JSON input to HTTP response
@Provider

public class ReaderExceptionMapper implements
        ExceptionMapper<org.jboss.resteasy.spi.ReaderException> {

    protected static Logger logger = Logger.getLogger(ReaderExceptionMapper.class);

    @Override
    public Response toResponse(ReaderException ex) {
        logger.error(ex.getMessage());
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}
