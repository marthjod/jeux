package de.fhb.jeux.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.ReaderException;

// used to map exceptions caused by bad client-provided JSON input to HTTP response 

@Provider
@SuppressWarnings("ucd")
public class ReaderExceptionMapper implements
		ExceptionMapper<org.jboss.resteasy.spi.ReaderException> {

	@Override
	public Response toResponse(ReaderException ex) {
		return Response.status(Response.Status.BAD_REQUEST).build();
	}

}
