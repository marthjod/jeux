package de.fhb.jeux.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.spi.ReaderException;

@Provider
public class ReaderExceptionMapper implements
		ExceptionMapper<org.jboss.resteasy.spi.ReaderException> {

	@Override
	public Response toResponse(ReaderException ex) {
		return Response.status(Response.Status.BAD_REQUEST).build();
	}

}
