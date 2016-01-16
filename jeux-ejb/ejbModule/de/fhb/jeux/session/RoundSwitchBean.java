package de.fhb.jeux.session;

import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.dao.RoundSwitchRuleDAO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.model.IRanking;
import de.fhb.jeux.model.IRoundSwitchRule;
import de.fhb.jeux.persistence.ShowdownRanking;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.jboss.logging.Logger;

@Stateless
public class RoundSwitchBean implements RoundSwitchRemote, RoundSwitchLocal {

    @EJB
    private GroupDAO groupDAO;

    @EJB
    private RoundSwitchRuleDAO ruleDAO;

    @EJB
    private AdHocRankingLocal adHocRankingBean;

    @EJB
    private FinalRankingLocal finalRankingBean;

    private static Logger logger = Logger.getLogger(RoundSwitchBean.class);

    // 201 Created
    public static final int TAKEOVER_OK = 200;
    // 204 No Content
    public static final int NOTHING_TO_DO = 204;
    // 409 Cconflict
    public static final int DEST_GROUP_CONFLICT = 409;
    // 428 Precondition Required
    public static final int SRC_GROUP_CONFLICT = 428;
    // 500 Internal Server Error
    static final int UNKNOWN_ERR = 500;

    public RoundSwitchBean() {
    }

    protected boolean checkDestGroup(IGroup destGroup) {
        boolean ok = false;

        if (destGroup != null) {
            if (destGroup.hasGames()) {
                logger.warn("Destination group " + destGroup
                        + " already contains games");
            } else if (destGroup.isCompleted()) {
                logger.warn("Destination group " + destGroup
                        + " is completed already");
            } else {
                ok = true;
            }
        } else {
            logger.error("Destination group " + destGroup
                    + " does not exist");
        }

        return ok;
    }

    protected boolean checkSrcGroups(IGroup destGroup, List<IRoundSwitchRule> rules) {
        boolean ok = true;

        if (!rules.isEmpty()) {
            for (IRoundSwitchRule rule : rules) {
                IGroup srcGroup = rule.getSrcGroup();
                if (!srcGroup.isCompleted()) {
                    ok = false;
                    logger.warn("Source group " + srcGroup + " not completed yet");
                    break;
                }
            }
        } else {
            ok = false;
        }

        return ok;
    }

    private void doFinalRanking(List<IPlayer> rankedPlayers) {
        for (IPlayer player : rankedPlayers) {
            IRanking ranking = new ShowdownRanking(player, player.getGroup());
            finalRankingBean.addRanking(ranking);
        }
    }

    private void applyRule(IGroup destGroup, IRoundSwitchRule rule) {
        IGroup srcGroup = rule.getSrcGroup();
        List<IPlayer> rankedPlayers = adHocRankingBean.getRankedPlayers(srcGroup);
        doFinalRanking(rankedPlayers);
        srcGroup.setInactive();

        // TODO moving players fails if higher-ranked player cannot
        // be determined definitively
        for (int rank = rule.getStartWithRank() - 1; rank < (rule
                .getStartWithRank() + rule.getAdditionalPlayers()); rank++) {

            IPlayer rankedPlayer = rankedPlayers.get(rank);
            rankedPlayer.setGroup(destGroup);
            rankedPlayer.resetStats();

            logger.info("Applied " + rule);
        }
    }

    @Override
    public int doTakeover(IGroup destGroup) {
        int status = UNKNOWN_ERR;

        if (checkDestGroup(destGroup)) {
            List<IRoundSwitchRule> rules = ruleDAO.getRulesForDestGroup(destGroup);

            if (!rules.isEmpty()) {
                logger.info("Starting takeover for " + destGroup);

                if (checkSrcGroups(destGroup, rules)) {

                    for (IRoundSwitchRule rule : rules) {
                        applyRule(destGroup, rule);
                    }
                    destGroup.setActive();
                    status = TAKEOVER_OK;
                    logger.info("Takeover for " + destGroup + " finished successfully");
                } else {
                    status = SRC_GROUP_CONFLICT;
                    logger.warn("Takeover for " + destGroup
                            + " failed: source group(s) conflict");
                }
            } else {
                status = NOTHING_TO_DO;
                logger.info("No rules found for " + destGroup);
            }
        } else {
            status = DEST_GROUP_CONFLICT;
            logger.warn("Takeover for " + destGroup
                    + " failed: destination group conflict");
        }

        return status;
    }
}
