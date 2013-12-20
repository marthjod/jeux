package de.fhb.jeux.config;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

// reads/converts bonus points rules and provides convenient access
public class BonusPointsDistributor {

	protected static Logger logger = Logger
			.getLogger(BonusPointsDistributor.class);

	// singleton
	private static BonusPointsDistribution config = null;

	public static HashMap<String, Integer> getBonusPoints(
			String bonusPointsConfigPath, int totalSetsPlayed,
			int setsWonByWinner) {

		HashMap<String, Integer> bonusPoints = new HashMap<String, Integer>();
		// defaults
		bonusPoints.put("winner", 0);
		bonusPoints.put("loser", 0);

		BonusPointsDistribution config = getBonusPointsDistribution(bonusPointsConfigPath);
		if (config != null) {
			for (BonusPointsRule rule : config.getRules()) {
				if (totalSetsPlayed == rule.getTotalSets()
						&& setsWonByWinner == rule.getSetsWonByWinner()) {
					// award bonus points accordingly
					bonusPoints.put("winner", rule.getBonusPointsWinner());
					bonusPoints.put("loser", rule.getBonusPointsLoser());
				}
			}
		}

		if (bonusPoints.get("winner") == 0 && bonusPoints.get("loser") == 0) {
			logger.warn("No bonus points awarded.");
		}

		return bonusPoints;
	}

	private static BonusPointsDistribution getBonusPointsDistribution(
			String bonusPointsConfigPath) {
		// singleton

		// static field
		if (config == null) {

			BufferedReader br = null;
			String json = null;

			try {
				br = new BufferedReader(new FileReader(bonusPointsConfigPath));
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
					logger.error("Error reading from config file "
							+ bonusPointsConfigPath + ": " + e.getMessage());
				} finally {
					try {
						br.close();
					} catch (IOException e) {
						logger.error(e.getClass().getName() + ": "
								+ e.getMessage());
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
					logger.error("Error parsing config file "
							+ bonusPointsConfigPath + ": " + e.getMessage());
				}
			}
		}

		return config;
	}
}
