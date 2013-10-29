package de.fhb.jeux.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import de.fhb.jeux.session.CreatePlayerLocal;

public class CreatePlayerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected static Logger logger = Logger
			.getLogger(CreatePlayerServlet.class);

	@EJB
	private CreatePlayerLocal createPlayerBean;

	public CreatePlayerServlet() {
		super();
	}

	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		boolean playerCreated = false;
		int groupId;

		BufferedReader requestBody = new BufferedReader(request.getReader());
		StringBuilder jsonData = new StringBuilder();
		String s = "";
		while ((s = requestBody.readLine()) != null) {
			jsonData.append(s);
		}

		try {
			// get trailing number after last slash in URL
			groupId = Integer.parseInt(
					request.getRequestURL().substring(
							request.getRequestURL().lastIndexOf("/") + 1), 10);
			// logger.debug("Group ID from request URL = " + groupId);
			playerCreated = createPlayerBean.createPlayer(jsonData.toString(),
					groupId);
		} catch (NumberFormatException e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
		}

		if (playerCreated) {
			response.setStatus(HttpServletResponse.SC_CREATED);
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
}
