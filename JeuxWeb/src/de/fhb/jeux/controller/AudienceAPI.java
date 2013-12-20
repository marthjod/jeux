package de.fhb.jeux.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import de.fhb.jeux.dto.GameDTO;
import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.dto.RoundSwitchRuleDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.session.GameLocal;
import de.fhb.jeux.session.GameSetLocal;
import de.fhb.jeux.session.GroupLocal;
import de.fhb.jeux.session.RoundSwitchRuleLocal;

@Stateless
@Path("/rest/audience")
public class AudienceAPI {

	protected static Logger logger = Logger.getLogger(AudienceAPI.class);

	@EJB
	private GroupLocal groupBean;

	@EJB
	private GameLocal gameBean;

	@EJB
	private GameSetLocal gameSetBean;

	@EJB
	private RoundSwitchRuleLocal roundSwitchRuleBean;

	@GET
	@Path("/status")
	@Produces(MediaType.TEXT_PLAIN)
	public String apiStatus() {
		// logger.debug("-> ReST API status");
		return "OK\n";
	}

	@GET
	@Path("/groups")
	@Produces(MediaType.APPLICATION_JSON)
	public List<GroupDTO> getAllGroupDTOs() {
		// logger.debug("-> All groups");
		return groupBean.getAllGroupDTOs();
	}

	@GET
	@Path("/roundswitchrules")
	@Produces(MediaType.APPLICATION_JSON)
	public List<RoundSwitchRuleDTO> getAllRoundSwitchRuleDTOs() {
		// logger.debug("-> All rules");
		return roundSwitchRuleBean.getAllRoundSwitchRuleDTOs();
	}

	@GET
	@Path("/players/group/id/{groupId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<PlayerDTO> getPlayerDTOsInGroup(
			@PathParam("groupId") int groupId) {
		// logger.debug("-> Players in group #" + groupId);
		IGroup group = groupBean.getGroupById(groupId);
		return groupBean.getPlayerDTOsInGroup(group);
	}

	@GET
	@Path("/games/played/group/id/{groupId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<GameDTO> getPlayedGameDTOsInGroup(
			@PathParam("groupId") int groupId) {
		// logger.debug("-> Played games for group #" + groupId);
		IGroup group = groupBean.getGroupById(groupId);
		return gameBean.getPlayedGameDTOsInGroup(group);
	}

	@GET
	@Path("/games/unplayed/group/id/{groupId}")
	@Produces(MediaType.APPLICATION_JSON)
	public List<GameDTO> getUnplayedGameDTOsInGroup(
			@PathParam("groupId") int groupId) {
		// logger.debug("-> Unplayed games for group #" + groupId);
		IGroup group = groupBean.getGroupById(groupId);
		return gameBean.getUnplayedGameDTOsInGroup(group);
	}
}
