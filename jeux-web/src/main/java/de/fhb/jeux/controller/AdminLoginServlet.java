package de.fhb.jeux.controller;

import de.fhb.jeux.util.AuthUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.jboss.logging.Logger;

public class AdminLoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    protected static Logger logger = Logger.getLogger(AdminLoginServlet.class);

    public AdminLoginServlet() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {

        String nextView = response.encodeRedirectURL(request.getContextPath());

        // TODO add some real #security
        String serverSecret = getServletConfig().getInitParameter(
                "SERVER_SECRET");

        if (serverSecret == null || serverSecret.length() == 0) {
            logger.error("Mandatory server secret not set, unable to generate auth tokens");
        } else {
            // TODO different user names #security
            // TODO different cookies for different users/access times #security
            String authToken = AuthUtils.generateAuthToken("admin", serverSecret);

            if (authToken.length() > 0) {
                // TODO no hardcoded naming #app-design
                Cookie authCookie = new Cookie("admin-auth", authToken);
                logger.debug("Returning cookie " + authCookie.getName() + "="
                        + authCookie.getValue());
                response.addCookie(authCookie);
                // TODO no hardcoded naming #app-design
                nextView = response.encodeRedirectURL(request.getContextPath()
                        + "/admin.html");
            } else {
                logger.warn("Could not generate authentification token.");
            }
        }

        response.sendRedirect(nextView);
    }
}
