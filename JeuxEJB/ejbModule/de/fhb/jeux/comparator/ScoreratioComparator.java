package de.fhb.jeux.comparator;

import java.util.Comparator;

import org.jboss.logging.Logger;

import de.fhb.jeux.dto.PlayerDTO;

public class ScoreratioComparator implements Comparator<PlayerDTO> {

	protected static Logger logger = Logger
			.getLogger(ScoreratioComparator.class);

	@Override
	public int compare(PlayerDTO p1, PlayerDTO p2) {
		PlayerDTO higherRank = null;

		if (p1.getScoreRatio() > p2.getScoreRatio()) {
			higherRank = p1;
		} else if (p1.getScoreRatio() < p2.getScoreRatio()) {
			higherRank = p2;
		} else {
			// direct comparison necessary...
			// TODO
			// find game in which both have played and if exists
			// check for winner
		}

		if (p1.equals(higherRank)) {
			logger.info(p1.getName() + " > " + p2.getName());
			return -1;
		} else if (p2.equals(higherRank)) {
			logger.info(p2.getName() + " > " + p1.getName());
			return 1;
		} else {
			logger.warn("Could not determine winner");
			return 0;
		}
	}
}
