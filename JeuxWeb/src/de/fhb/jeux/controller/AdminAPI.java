package de.fhb.jeux.controller;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.dto.RoundSwitchRuleDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.session.CreateGroupLocal;
import de.fhb.jeux.session.CreatePlayerLocal;
import de.fhb.jeux.session.CreateRoundSwitchRuleLocal;
import de.fhb.jeux.session.DeleteGroupLocal;
import de.fhb.jeux.session.GroupLocal;
import de.fhb.jeux.session.RoundSwitchRuleLocal;

@Stateless
@Path("/rest/admin")
public class AdminAPI {

	protected static Logger logger = Logger.getLogger(AdminAPI.class);

	@EJB
	private GroupLocal groupBean;

	@EJB
	private CreatePlayerLocal createPlayerBean;

	@EJB
	private CreateGroupLocal createGroupBean;

	@EJB
	private DeleteGroupLocal deleteGroupBean;

	@EJB
	private CreateRoundSwitchRuleLocal createRoundSwitchRuleBean;

	@EJB
	private RoundSwitchRuleLocal roundSwitchRuleBean;

	@DELETE
	@Path("/delete-group/id/{groupId}")
	public Response deleteGroup(@PathParam("groupId") int groupId) {
		logger.debug("Request for group deletion");

		int result = deleteGroupBean.deleteGroup(groupBean
				.getGroupById(groupId));
		if (GroupDAO.DELETION_OK == result) {
			return Response.status(Response.Status.OK).build();
		} else if (GroupDAO.DELETION_CONFLICT == result) {
			return Response.status(Response.Status.FORBIDDEN).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	@PUT
	@Path("/create-group")
	@Consumes(MediaType.APPLICATION_JSON)
	// Must use instantiable param for underlying Jackson here.
	// see de.fhb.jeux.exception for ExceptionMappers as described in
	// http://docs.jboss.org/resteasy/docs/1.2.GA/userguide/html/ExceptionHandling.html
	public Response createGroup(GroupDTO groupDTO) {
		logger.debug("Request for group creation");
		if (groupDTO != null) {
			logger.debug("Deserialized group DTO " + groupDTO);
			createGroupBean.createGroup(groupDTO);
			return Response.status(Response.Status.CREATED).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	@PUT
	@Path("/create-player")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createPlayer(PlayerDTO playerDTO) {
		logger.debug("Request for player creation");

		if (playerDTO != null) {
			logger.debug("Deserialized player DTO " + playerDTO);

			// we look for the group object here already, because CDI
			// implementation sucks.
			// also, we can decide to reject the player if the group does not
			// exist yet
			IGroup group = groupBean.getGroupById(playerDTO.getGroupId());
			createPlayerBean.createPlayer(playerDTO, group);
			return Response.status(Response.Status.CREATED).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}

	@PUT
	@Path("/create-roundswitchrule")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createRoundSwitchRule(RoundSwitchRuleDTO rule) {
		logger.debug("Request for rule creation");

		boolean createdSuccessfully = false;

		if (rule != null) {
			logger.debug("Deserialized round switch rule DTO " + rule);
			IGroup srcGroup = groupBean.getGroupById(rule.getSrcGroupId());
			IGroup destGroup = groupBean.getGroupById(rule.getDestGroupId());
			if (createRoundSwitchRuleBean.createRoundSwitchRule(rule, srcGroup,
					destGroup)) {
				createdSuccessfully = true;
			}
		}

		if (createdSuccessfully) {
			return Response.status(Response.Status.CREATED).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}
}
