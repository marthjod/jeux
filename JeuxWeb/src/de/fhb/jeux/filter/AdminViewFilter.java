package de.fhb.jeux.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;

import de.fhb.jeux.util.AuthUtils;

public class AdminViewFilter implements Filter {

	protected static Logger logger = Logger.getLogger(AdminViewFilter.class);

	String serverSecret;

	public AdminViewFilter() {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {

		/*
		 * JEE Filter#doFilter() comes only with ServletRequest,
		 * ServletResponse, not their HttpServletX counterparts; cast to
		 * HttpServletRequest necessary in order to be able to use
		 * getContextPath(), getCookies() etc. cast to HttpServletResponse
		 * necessary in order to be able to use sendRedirect()
		 * 
		 * some methods are missing, namely HttpServletRequest.getPathInfo();
		 * fortunately, we can simulate this via ServletRequest.getServletPath
		 * with the context part removed
		 */

		if (request instanceof HttpServletRequest) {

			String req = ((HttpServletRequest) request).getMethod() + " "
					+ ((HttpServletRequest) request).getRequestURI().toString();

			boolean userAuthenticated = false;
			Cookie[] cookies = ((HttpServletRequest) request).getCookies();
			Cookie authCookie = null;

			if (cookies != null) {
				for (Cookie cookie : cookies) {

					// TODO naming
					if ("admin-auth".equals(cookie.getName())) {
						authCookie = cookie;
						// we got what we came for
						break;
					}
				}

				// TODO see AdminLoginServlet
				if (authCookie != null
						&& AuthUtils.checkAuthTokens("admin", serverSecret,
								authCookie.getValue())) {
					userAuthenticated = true;
				}
			}

			if (userAuthenticated) {
				logger.debug("OK: " + req + " (cookie '" + authCookie.getName()
						+ "=" + authCookie.getValue() + "')");
				// OK, move along
				chain.doFilter(request, response);
			} else {
				// !userAuthenticated
				logger.warn("Authentication failed for " + req);
				if (authCookie != null) {
					logger.warn("Provided cookie: " + authCookie.getName()
							+ "=" + authCookie.getValue());
				}

				// 403 Forbidden
				((HttpServletResponse) response)
						.setStatus(HttpServletResponse.SC_FORBIDDEN);
			}
		}
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		// get SERVER_SECRET from web.xml
		serverSecret = fConfig.getInitParameter("SERVER_SECRET");
	}

}
