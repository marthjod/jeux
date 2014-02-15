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

		for (IGroup group : groupDAO.getGroupsInRound(roundId, true)) {
			// find appropriate round-switch rules (RSRs)
			for (IRoundSwitchRule rule : ruleDAO.getRulesForSrcGroup(group)) {
				// does the destination group exist?
				if (groupDAO.getGroupById(rule.getDestGroup().getId()) != null) {
					// TODO dest group should be incomplete, without games or
					// players etc.

					List<IPlayer> rankedPlayers = rankingBean
							.getRankedPlayers(group);

					// list index starts at 0 (= rank 1)
					// move players according to ranking
					logger.info("Moving " + (1 + rule.getAdditionalPlayers())
							+ " players according to RSR " + rule);

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
					// TODO destination group does not exist
				}
			}
		}

		return success;
	}
}
