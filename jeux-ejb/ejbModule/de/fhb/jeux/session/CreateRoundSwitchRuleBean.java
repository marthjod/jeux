package de.fhb.jeux.session;

import de.fhb.jeux.dao.RoundSwitchRuleDAO;
import de.fhb.jeux.dto.RuleDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IRoundSwitchRule;
import de.fhb.jeux.persistence.ShowdownRoundSwitchRule;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.jboss.logging.Logger;

@Stateless

public class CreateRoundSwitchRuleBean implements CreateRoundSwitchRuleRemote,
        CreateRoundSwitchRuleLocal {

    protected static Logger logger = Logger
            .getLogger(CreateRoundSwitchRuleBean.class);

    public static final int STATUS_OK = 201;
    // Not Acceptable
    public static final int RANK_TOO_LOW = 406;
    // Conflict
    public static final int SRC_GROUP_EQUALS_DEST_GROUP = 409;
    // Precondition Failed
    public static final int SRC_OR_DEST_GROUP_NONEXISTANT = 412;
    // Request Entity Too Large
    public static final int TOO_MANY_PLAYERS_TO_BE_MOVED = 413;
    // Requested Range Not Satisfiable
    public static final int RANK_EXCEEDS_GROUP_SIZE = 416;
    public static final int UNKNOWN_ERR = 500;

    @EJB
    private RoundSwitchRuleDAO ruleDAO;

    @EJB
    private GroupLocal groupBean;

    public CreateRoundSwitchRuleBean() {
    }

    @Override
    public int createRoundSwitchRule(RuleDTO ruleDTO,
            IGroup srcGroup, IGroup destGroup) {

        int status = UNKNOWN_ERR;

        if (srcGroup != null && destGroup != null) {
            if (!srcGroup.equals(destGroup)) {
                IRoundSwitchRule newRule = new ShowdownRoundSwitchRule(ruleDTO,
                        srcGroup, destGroup);
				// int srcGroupSize = srcGroup.getSize();

                // NOTE: some checks have been removed to allow adding rules for
                // groups which do not yet have players, i.e. empty second-round
                // groups with destination in the third round
                if (ruleDTO.getStartWithRank() > 0) {
					// if (ruleDTO.getStartWithRank() <= srcGroupSize) {

                    // if (ruleDTO.getStartWithRank()
                    // + ruleDTO.getAdditionalPlayers() <= srcGroupSize) {
                    newRule.setSrcGroup(srcGroup);
                    newRule.setDestGroup(destGroup);
                    if (ruleDAO.addRule(newRule)) {
                        status = STATUS_OK;
                        logger.debug("Added round switch rule " + newRule + ".");
                    } else {
                        logger.debug("Adding round switch rule " + newRule
                                + " failed.");
                    }
                    // } else {
                    // status = TOO_MANY_PLAYERS_TO_BE_MOVED;
                    // logger.warn("Too many players to be moved (last considered rank "
                    // + (ruleDTO.getStartWithRank() + ruleDTO
                    // .getAdditionalPlayers())
                    // + " > source group size of "
                    // + srcGroupSize
                    // + ").");
                    // }
                    // } else {
                    // status = RANK_EXCEEDS_GROUP_SIZE;
                    // logger.warn("A rank exceeds the group's size.");
                    // }
                } else {
                    status = RANK_TOO_LOW;
                    logger.debug("Must start at least at rank 1.");
                }
            } else {
                status = SRC_GROUP_EQUALS_DEST_GROUP;
                logger.debug("Source group equals destination group.");
            }
        } else {
            status = SRC_OR_DEST_GROUP_NONEXISTANT;
            logger.debug("Source or destination group is null.");
        }

        return status;
    }
}
