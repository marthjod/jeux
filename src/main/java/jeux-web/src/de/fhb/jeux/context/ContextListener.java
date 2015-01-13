package de.fhb.jeux.context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.io.FileUtils;
import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import de.fhb.jeux.config.BonusPointsDistribution;
import de.fhb.jeux.config.BonusPointsRule;

public class ContextListener implements ServletContextListener {

	protected static Logger logger = Logger.getLogger(ContextListener.class);

	public ContextListener() {
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {

		logger.info("Context initialized");
		ServletContext sc = event.getServletContext();
		String scoresheetsLaTeXPreamble = null;
		String scoresheetsLaTeXBody = null;
		String scoresheetsLaTeXFooter = null;

		// get infos from web.xml and save object to servlet context
		// to be accessed by other servlets later
		// avoiding I/O overhead by reading config file only once at startup

		BonusPointsDistribution config = getBonusPointsConfig(sc.getRealPath(sc
				.getInitParameter("BONUS_POINTS_CONFIG_PATH")));

		try {
			scoresheetsLaTeXPreamble = FileUtils.readFileToString(new File(sc
					.getRealPath(sc
							.getInitParameter("SCORESHEETS_LATEX_PREAMBLE"))));
			scoresheetsLaTeXBody = FileUtils
					.readFileToString(new File(sc.getRealPath(sc
							.getInitParameter("SCORESHEETS_LATEX_BODY"))));
			scoresheetsLaTeXFooter = FileUtils.readFileToString(new File(sc
					.getRealPath(sc
							.getInitParameter("SCORESHEETS_LATEX_FOOTER"))));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		sc.setAttribute("bonusPointsConfig", config);
		sc.setAttribute("scoresheetsLaTeXPreamble", scoresheetsLaTeXPreamble);
		sc.setAttribute("scoresheetsLaTeXBody", scoresheetsLaTeXBody);
		sc.setAttribute("scoresheetsLaTeXFooter", scoresheetsLaTeXFooter);

	}

	private BonusPointsDistribution getBonusPointsConfig(String configPath) {
		String json = null;
		BonusPointsDistribution config = null;

		try {
			json = FileUtils.readFileToString(new File(configPath));
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		if (json != null) {
			Gson gson = new Gson();
			try {
				// read JSON array into ArrayList
				ArrayList<BonusPointsRule> rules = gson.fromJson(json,
						new TypeToken<List<BonusPointsRule>>() {
						}.getType());
				// create BonusPointsDistribution from list
				config = new BonusPointsDistribution(rules);
			} catch (JsonSyntaxException e) {
				logger.error("Error parsing config file " + configPath + ": "
						+ e.getMessage());
			}
		}

		return config;
	}
}