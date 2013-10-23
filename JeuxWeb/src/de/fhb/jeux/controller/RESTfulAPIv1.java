package de.fhb.jeux.controller;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.jboss.logging.Logger;

import com.google.gson.Gson;

import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.session.DeleteGroupLocal;
import de.fhb.jeux.session.GroupLocal;

@Stateless
@Path("/rest/v1")
public class RESTfulAPIv1 {

	protected static Logger logger = Logger.getLogger(RESTfulAPIv1.class);

	@EJB
	private GroupLocal groupBean;

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
	@Produces(MediaType.TEXT_PLAIN)
	public String allGroups() {
		StringBuilder sb = new StringBuilder();
		List<IGroup> groups = groupBean.getAllGroups();
		for (IGroup group : groups) {
			sb.append(group.toString() + "\n");
		}
		return sb.toString();
	}

	@GET
	@Path("/groups-json")
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
}
