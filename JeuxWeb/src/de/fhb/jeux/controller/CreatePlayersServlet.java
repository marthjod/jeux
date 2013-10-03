package de.fhb.jeux.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import de.fhb.jeux.session.CreatePlayersLocal;

public class CreatePlayersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected static Logger logger = Logger
			.getLogger(CreatePlayersServlet.class);

	@EJB
	private CreatePlayersLocal createPlayersBean;

	public CreatePlayersServlet() {
		super();
	}

	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		boolean playersCreated = false;

		BufferedReader requestBody = new BufferedReader(request.getReader());
		StringBuilder jsonData = new StringBuilder();
		String s = "";
		while ((s = requestBody.readLine()) != null) {
			jsonData.append(s);
		}

		playersCreated = createPlayersBean.createPlayers(jsonData.toString());

		if (playersCreated) {
			response.setStatus(HttpServletResponse.SC_CREATED);
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

}
