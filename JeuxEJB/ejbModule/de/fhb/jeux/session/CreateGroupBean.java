package de.fhb.jeux.session;

import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import de.fhb.jeux.json.IPlayerAdapter;
import de.fhb.jeux.mockentity.MockGroupEntity;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;

@Stateless
public class CreateGroupBean implements CreateGroupRemote, CreateGroupLocal {

	protected static Logger logger = Logger.getLogger(CreateGroupBean.class);

	private Gson gson;

	public CreateGroupBean() {
		// see
		// de.fhb.jeux.json.IPlayerAdapter.java
		this.gson = new GsonBuilder().registerTypeAdapter(IPlayer.class,
				new IPlayerAdapter().nullSafe()).create();
	}

	@Override
	public boolean createNewGroup(String jsonRepresentation) {
		boolean success = false;
		IGroup group;

		try {
			group = gson.fromJson(jsonRepresentation, MockGroupEntity.class);
			success = true;
			// logger.debug("\n" + gson.toJson(group));
			logger.debug("Created group " + group.toString());
		} catch (JsonIOException e) {
			logger.error("JSON I/O error");
			// TODO
		} catch (JsonSyntaxException e) {
			logger.error("JSON syntax error");
			// TODO
		} catch (JsonParseException e) {
			logger.error("JSON parse error");
			// TODO
		} catch (Exception e) {
			// TODO Pokémon
			logger.error("Exception: " + e.getMessage());
		}

		if (!success) {
			logger.error("Failed to create group from JSON input '"
					+ jsonRepresentation + "'");
		}

		return success;
	}
}