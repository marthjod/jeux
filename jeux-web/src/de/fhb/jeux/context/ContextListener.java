package de.fhb.jeux.context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

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

		// get infos from web.xml and save object to servlet context
		// to be accessed by other servlets later
		// avoiding I/O overhead by reading config file only once at startup

		BonusPointsDistribution config = getBonusPointsConfig(sc.getRealPath(sc
				.getInitParameter("BONUS_POINTS_CONFIG_PATH")));

		sc.setAttribute("bonusPointsConfig", config);
	}

	private BonusPointsDistribution getBonusPointsConfig(String configPath) {
		BufferedReader br = null;
		String json = null;
		BonusPointsDistribution config = null;

		try {
			br = new BufferedReader(new FileReader(configPath));
			logger.info("Will read from config file " + configPath);
		} catch (FileNotFoundException e) {
			logger.error(e.getClass().getName() + ": " + e.getMessage());
		}

		if (br != null) {
			try {
				StringBuilder sb = new StringBuilder();
				String line = br.readLine();

				while (line != null) {
					sb.append(line);
					sb.append('\n');
					line = br.readLine();
				}
				json = sb.toString();
			} catch (IOException e) {
				logger.error("Error reading from config file " + configPath
						+ ": " + e.getMessage());
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					logger.error(e.getClass().getName() + ": " + e.getMessage());
				}
			}
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