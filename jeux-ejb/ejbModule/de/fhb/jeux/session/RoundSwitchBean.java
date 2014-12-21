package de.fhb.jeux.session;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.dao.RoundSwitchRuleDAO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.model.IRoundSwitchRule;

@Stateless
public class RoundSwitchBean implements RoundSwitchRemote, RoundSwitchLocal {

	@EJB
	private GroupDAO groupDAO;

	@EJB
	private RoundSwitchRuleDAO ruleDAO;

	@EJB
	private RankingLocal rankingBean;

	private static Logger logger = Logger.getLogger(RoundSwitchBean.class);

	public RoundSwitchBean() {
	}

	@Override
	public boolean switchRound(int roundId) {
		boolean success = false;

		logger.info("--- Switching round... ---");

		for (IGroup group : groupDAO.getGroupsInRound(roundId, true)) {

			// rankings done once before players get moved
			List<IPlayer> rankedPlayers = rankingBean.getRankedPlayers(group);

			// find appropriate round-switch rules (RSRs)
			for (IRoundSwitchRule rule : ruleDAO.getRulesForSrcGroup(group)) {
				IGroup destGroup = rule.getDestGroup();
				// dest group should be incomplete, without games or
				// players etc.
				if (groupDAO.hasGames(destGroup)) {
					logger.warn("Destination group " + destGroup
							+ " already contains games");
					// break;
				} else if (destGroup.isCompleted()) {
					logger.warn("Destination group " + destGroup
							+ " is completed already");
					// break;
				} else if (destGroup.isActive()) {
					// logger.info("Destination group " + destGroup
					// + " is active already");
				} else if (destGroup.getPlayers().size() > 0) {
					// logger.info("Destination group " + destGroup
					// + " already has players");
					// break;
				}
				// does the destination group exist?
				if (groupDAO.getGroupById(destGroup.getId()) != null) {

					destGroup.setActive(true);

					// list index starts at 0 (= rank 1)
					// move players according to ranking
					logger.info("Applying RSR " + rule);

					// TODO moving players fails if higher-ranked player cannot
					// be determined definitively
					for (int rank = rule.getStartWithRank() - 1; rank < (rule
							.getStartWithRank() + rule.getAdditionalPlayers()); rank++) {

						IPlayer rankedPlayer = rankedPlayers.get(rank);
						// move players to destination group
						rankedPlayer.setGroup(rule.getDestGroup());

						// tabula rasa: clear won games, bonus points, points
						// ratio, rank
						rankedPlayer.setWonGames(0);
						rankedPlayer.setPoints(0);
						rankedPlayer.setRank(0);
						rankedPlayer.setScoreRatio(0);

						logger.info(rankedPlayers.get(rank).getName() + " ("
								+ group.getName() + "'s #" + (rank + 1)
								+ ") --> " + rule.getDestGroup().getName());
					}

				} else {
					logger.error("Destination group " + destGroup
							+ " does not exist");
				}
			}
		}

		logger.info("--- Round-switch finished. ---");
		return success;
	}
}
