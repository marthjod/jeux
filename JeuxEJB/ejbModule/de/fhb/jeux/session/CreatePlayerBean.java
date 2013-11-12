package de.fhb.jeux.session;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import de.fhb.jeux.mockentity.MockPlayerEntity;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownPlayer;

@Stateless
public class CreatePlayerBean implements CreatePlayerRemote, CreatePlayerLocal {

	protected static Logger logger = Logger.getLogger(CreatePlayerBean.class);

	@EJB
	private GroupLocal groupBean;

	private Gson gson;

	public CreatePlayerBean() {
		this.gson = new GsonBuilder().create();
	}

	@Override
	public boolean createPlayer(String jsonRepresentation, int groupId) {
		boolean success = false;

		try {
			// We must use an IPlayer implementation class (no interface) here.
			IPlayer player = gson.fromJson(jsonRepresentation,
					ShowdownPlayer.class);
			player.setGroup(groupBean.getGroupById(groupId));
			success = true;
			logger.debug("Created player " + player.getName() + " in group "
					+ player.getGroup().getName());
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
			logger.error("Failed to create player from JSON input '"
					+ jsonRepresentation + "'");
		}
		return success;
	}
}
