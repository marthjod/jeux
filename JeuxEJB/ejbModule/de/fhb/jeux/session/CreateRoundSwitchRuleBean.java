package de.fhb.jeux.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.RoundSwitchRuleDAO;
import de.fhb.jeux.dto.RoundSwitchRuleDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IRoundSwitchRule;
import de.fhb.jeux.persistence.ShowdownRoundSwitchRule;

@Stateless
@SuppressWarnings("ucd")
public class CreateRoundSwitchRuleBean implements CreateRoundSwitchRuleRemote,
		CreateRoundSwitchRuleLocal {

	protected static Logger logger = Logger
			.getLogger(CreateRoundSwitchRuleBean.class);

	@EJB
	private RoundSwitchRuleDAO ruleDAO;

	@EJB
	private GroupLocal groupBean;

	public CreateRoundSwitchRuleBean() {
	}

	@Override
	public boolean createRoundSwitchRule(RoundSwitchRuleDTO ruleDTO,
			IGroup srcGroup, IGroup destGroup) {

		boolean success = false;
		if (srcGroup != null && destGroup != null) {
			if (!srcGroup.equals(destGroup)) {
				IRoundSwitchRule newRule = new ShowdownRoundSwitchRule(ruleDTO,
						srcGroup, destGroup);
				int srcGroupSize = srcGroup.getSize();

				if (ruleDTO.getStartWithRank() > 0) {
					if (ruleDTO.getStartWithRank() <= srcGroupSize) {

						if (ruleDTO.getStartWithRank()
								+ ruleDTO.getAdditionalPlayers() <= srcGroupSize) {

							newRule.setSrcGroup(srcGroup);
							newRule.setDestGroup(destGroup);
							if (ruleDAO.addRoundSwitchRule(newRule)) {
								success = true;
								logger.info("Added round switch rule "
										+ newRule + ".");
							} else {
								logger.warn("Adding round switch rule "
										+ newRule + " failed.");
							}
						} else {
							logger.warn("Too many players to be moved (last considered rank "
									+ (ruleDTO.getStartWithRank() + ruleDTO
											.getAdditionalPlayers())
									+ " > source group size of "
									+ srcGroupSize
									+ ").");
						}
					} else {
						logger.warn("Rank exceeds group size.");
					}
				} else {
					logger.warn("Must start at least at rank 1.");
				}
			} else {
				logger.warn("Source group equals destination group.");
			}
		} else {
			logger.error("Source or destination group is null.");
		}

		return success;
	}
}
