package de.fhb.jeux.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import de.fhb.jeux.session.CreateNewGroupLocal;

public class CreateNewGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static Logger logger = Logger
			.getLogger(CreateNewGroupServlet.class);

	@EJB
	private CreateNewGroupLocal createNewGroupBean;

	public CreateNewGroupServlet() {
		super();
	}

	/*
	 * Sets HTTP response status code to 201 Created if group entity has been
	 * successfully created, 500 Internal Server Error otherwise.
	 */
	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		boolean groupCreated = false;

		BufferedReader requestBody = new BufferedReader(request.getReader());
		StringBuilder jsonData = new StringBuilder();
		String s = "";
		while ((s = requestBody.readLine()) != null) {
			jsonData.append(s);
		}

		logger.debug("Received JSON data: '" + jsonData.toString() + "'");

		groupCreated = createNewGroupBean.createNewGroup(jsonData.toString());
		if (groupCreated) {
			response.setStatus(HttpServletResponse.SC_CREATED);
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
