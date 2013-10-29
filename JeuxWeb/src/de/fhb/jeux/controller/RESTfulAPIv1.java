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

import com.google.gson.Gson;

import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.persistence.ShowdownGroup;
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

	private Gson gson;

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
	public String allGroupsJson() {
		gson = new Gson();
		StringBuilder jsonData = new StringBuilder();
		// open JS array
		jsonData.append("[");

		List<IGroup> groups = groupBean.getAllGroups();
		for (int i = 0; i < groups.size(); i++) {
			// add JS object to JS array
			jsonData.append(gson.toJson(groups.get(i)));
			// add comma except after last element
			if (i < groups.size() - 1) {
				jsonData.append(",");
			}
		}
		// close JS array
		jsonData.append("]");
		return jsonData.toString();
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
	// TODO register Exception mapper
	// http://docs.jboss.org/resteasy/docs/1.2.GA/userguide/html/ExceptionHandling.html
	public Response createGroup(ShowdownGroup group) {
		if (group != null) {
			logger.debug("REST API deserialized group '" + group.getName()
					+ "'");
			createGroupBean.createNewGroup(group);
			return Response.status(Response.Status.CREATED).build();
		} else {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
					.build();
		}
	}
}
