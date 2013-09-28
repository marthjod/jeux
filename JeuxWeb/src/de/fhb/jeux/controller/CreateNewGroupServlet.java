package de.fhb.jeux.controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

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

	@Override
	protected void doPut(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		BufferedReader requestBody = new BufferedReader(request.getReader());
		StringBuilder jsonData = new StringBuilder();
		String s = "";
		while ((s = requestBody.readLine()) != null) {
			jsonData.append(s);
		}

		createNewGroupBean.createNewGroup(jsonData.toString());

	}

}
