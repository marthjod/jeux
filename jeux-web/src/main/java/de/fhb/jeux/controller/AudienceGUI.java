package de.fhb.jeux.controller;

import com.mitchellbosecke.pebble.template.PebbleTemplate;
import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.session.AdHocRankingLocal;
import de.fhb.jeux.session.FinalRankingLocal;
import de.fhb.jeux.session.GameLocal;
import de.fhb.jeux.session.GroupLocal;
import de.fhb.jeux.session.PlayerLocal;
import de.fhb.jeux.template.Template;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.jboss.logging.Logger;

@Stateless
@Path("/gui/audience")
public class AudienceGUI {

    protected static Logger logger = Logger.getLogger(AudienceGUI.class);

    @Context
    HttpServletRequest request;

    @EJB
    private GroupLocal groupBean;

    @EJB
    private PlayerLocal playerBean;

    @EJB
    private GameLocal gameBean;

    @EJB
    private AdHocRankingLocal adHocRankingBean;

    @EJB
    private FinalRankingLocal finalRankingBean;

    private String getStreakInfo(List<GameDTO> games, IPlayer player) {

        int streakWon = 0;
        int streakLost = 0;

        if (games != null && player != null) {
            Iterator<GameDTO> gamesIterator = games.iterator();
            while (gamesIterator.hasNext()) {
                if (gamesIterator.next().getWinnerId() == player.getId()) {
                    if (streakLost == 0) {
                        streakWon++;
                        // logger.debug(player.getName() + " W" + streakWon);
                    } else {
                        // W becomes L or vice versa
                        // logger.debug("Streak broke for " + player.getName());
                        break;
                    }
                } else {
                    if (streakWon == 0) {
                        streakLost++;
                        // logger.debug(player.getName() + " L" + streakLost);
                    } else {
                        // W becomes L or vice versa
                        // logger.debug("Streak broke for " + player.getName());
                        break;
                    }
                }
            }
        }
        return streakWon > 0 ? "W" + streakWon : "L" + streakLost;
    }

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public String getOverview(@Context ServletContext servletContext) {
        Writer writer = new StringWriter();
        PebbleTemplate compiledTemplate = Template.getTemplate(servletContext, "audience-start");

        if (compiledTemplate != null) {
            Map<String, Object> context = new HashMap<>();
            context.put("prefix", "/" + servletContext.getServletContextName() + "/gui/audience");
            context.put("groups", groupBean.getAllGroupDTOs());
            writer = Template.renderTemplate(compiledTemplate, context);
        }

        return writer.toString();
    }

    @GET
    @Path("/rankings/group/{groupId}")
    @Produces(MediaType.TEXT_HTML)
    public String getRankingsInGroup(@Context ServletContext servletContext, @PathParam("groupId") int groupId) {
        Writer writer = new StringWriter();
        PebbleTemplate compiledTemplate = Template.getTemplate(servletContext, "audience-rankings");

        if (compiledTemplate != null) {
            Map<String, Object> context = new HashMap<>();
            IGroup group = groupBean.getGroupById(groupId);

            context.put("prefix", "/" + servletContext.getServletContextName() + "/gui/audience");
            if (group != null) {
                context.put("group", group);
                if (group.isActive()) {
                    context.put("rankings", adHocRankingBean.getRankedPlayerDTOs(group));
                } else {
                    context.put("rankings", finalRankingBean.getRankingDTOs(group));
                }
            } else {
                context.put("group", new GroupDTO("Group not found", false));
            }

            writer = Template.renderTemplate(compiledTemplate, context);
        }

        return writer.toString();
    }

    @GET
    @Path("/games/group/{groupId}")
    @Produces(MediaType.TEXT_HTML)
    public String getGamesInGroup(@Context ServletContext servletContext,
            @PathParam("groupId") int groupId) {
        Writer writer = new StringWriter();
        PebbleTemplate compiledTemplate = Template.getTemplate(servletContext, "audience-group-games");

        if (compiledTemplate != null) {
            Map<String, Object> context = new HashMap<>();
            IGroup group = groupBean.getGroupById(groupId);

            context.put("prefix", "/" + servletContext.getServletContextName() + "/gui/audience");
            if (group != null) {
                context.put("group", group);
                context.put("playedGames", gameBean.getPlayedGameDTOsInGroup(group));
                context.put("unplayedGames", gameBean.getUnplayedGameDTOsInGroup(group));
            } else {
                context.put("group", new GroupDTO("Group not found", false));
            }
            writer = Template.renderTemplate(compiledTemplate, context);
        }

        return writer.toString();
    }

    @GET
    @Path("/games/player/{playerId}")
    @Produces(MediaType.TEXT_HTML)
    public String getGamesForPlayer(@Context ServletContext servletContext,
            @PathParam("playerId") int playerId) {
        Writer writer = new StringWriter();
        PebbleTemplate compiledTemplate = Template.getTemplate(servletContext, "audience-player-games");

        if (compiledTemplate != null) {
            Map<String, Object> context = new HashMap<String, Object>();
            IPlayer player = playerBean.getPlayerById(playerId);

            context.put("prefix", "/" + servletContext.getServletContextName() + "/gui/audience");
            if (player != null) {
                // save extra DB call
                List<GameDTO> playedGames = playerBean.getPlayedGames(player);
                List<GameDTO> unplayedGames = playerBean.getUnplayedGames(player);

                int countPlayedGames = playedGames.size();
                Long countWonGames = playerBean.getCountWonGames(player);
                float pctgWon = 0.0f;
                // avoid NullPointer
                if (countPlayedGames > 0) {
                    pctgWon = (countWonGames * 100.0f) / countPlayedGames;
                }
                context.put("playerName", player.getName());
                context.put("playedGames", playedGames);
                context.put("unplayedGames", unplayedGames);
                context.put("countPlayedGames", countPlayedGames);
                context.put("wonGames", countWonGames);
                context.put("pctgWon", pctgWon);
                context.put("streak", getStreakInfo(playedGames, player));
            } else {
                context.put("playerName", "Player not found");
            }
            writer = Template.renderTemplate(compiledTemplate, context);
        }

        return writer.toString();
    }

}
