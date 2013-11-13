package de.fhb.jeux.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.logging.Logger;

import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.mockentity.MockRoundSwitchRuleEntity;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.session.CreateGroupLocal;
import de.fhb.jeux.session.CreatePlayerLocal;
import de.fhb.jeux.session.DeleteGroupLocal;
import de.fhb.jeux.session.GroupLocal;

@Stateless
@Path("/rest/v1")
public class RESTfulAPIv1 {

	protected static Logger logger = Logger.getLogger(RESTfulAPIv1.class);

	@EJB
	private GroupLocal groupBean;

	@EJB
	private CreatePlayerLocal createPlayerBean;

	@EJB
	private CreateGroupLocal createGroupBean;

	@EJB
	private DeleteGroupLocal deleteGroupBean;

	@GET
	@Path("/status")
	@Produces(MediaType.TEXT_PLAIN)
	public String apiStatus() {
		logger.debug("REST API status request");
		return "OK\n";
	}

	@GET
	@Path("/groups")
	@Produces(MediaType.APPLICATION_JSON)
	public List<IGroup> getAllGroups() {
		List<IGroup> groups = groupBean.getAllGroups();
		return groups;
	}

	@DELETE
	@Path("/delete-group/{groupId}")
	public void deleteGroup(@PathParam("groupId") int groupId) {
		deleteGroupBean.deleteGroup(groupBean.getGroupById(groupId));
	}

	// Test:
	// curl -X PUT -H "Content-Type: application/json" -d
	// '{"minSets":"1","maxSets":"1","name":"New
	// group","roundId":1,"completed":false,"active":true}'
	// http://localhost:8080/JeuxWeb/rest/v1/create-group
	@PUT
	@Path("/create-group")
	@Consumes(MediaType.APPLICATION_JSON)
	// Must use instantiable param for underlying Jackson here.
	// see de.fhb.jeux.exception for ExceptionMappers as described in
	// http://docs.jboss.org/resteasy/docs/1.2.GA/userguide/html/ExceptionHandling.html
	public Response createGroup(GroupDTO groupDTO) {
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
		if (playerDTO != null) {
			logger.debug("Deserialized player DTO " + playerDTO);

			// we look for the group object here already, because CDI sucks.
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

	// Test: curl -X PUT -H "Content-Type: application/json" -d
	// '{"srcGroupId":1, "destGroupId":2, "startWithRank":1,
	// "additionalPlayers":1}'
	// http://localhost:8080/JeuxWeb/rest/v1/create-roundswitchrule

	@PUT
	@Path("/create-roundswitchrule")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createRoundSwitchRule(MockRoundSwitchRuleEntity rule) {
		if (rule != null) {
			logger.debug("Deserialized round switch rule '" + rule.toString()
					+ "'");

			return Response.status(Response.Status.CREATED).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}
}
