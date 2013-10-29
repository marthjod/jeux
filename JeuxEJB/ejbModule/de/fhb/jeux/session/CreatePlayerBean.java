package de.fhb.jeux.session;

import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import de.fhb.jeux.mockentity.MockPlayerEntity;
import de.fhb.jeux.model.IPlayer;

@Stateless
public class CreatePlayerBean implements CreatePlayerRemote, CreatePlayerLocal {

	protected static Logger logger = Logger.getLogger(CreatePlayerBean.class);

	private Gson gson;

	public CreatePlayerBean() {
		// this.gson = new GsonBuilder().registerTypeAdapter(IPlayer.class,
		// new IPlayerAdapter().nullSafe()).create();
		this.gson = new GsonBuilder().create();
	}

	@Override
	public boolean createPlayer(String jsonRepresentation) {
		boolean success = false;

		// List<IPlayer> players;

		try {
			// http://stackoverflow.com/questions/4318458/how-to-deserialize-a-list-using-gson-or-another-json-to-java
			// players = gson.fromJson(jsonRepresentation,
			// new TypeToken<List<MockPlayerEntity>>() {
			// }.getType());

			// We must use ShowdownPlayer.class (no interface) here.
			IPlayer player = gson.fromJson(jsonRepresentation,
					MockPlayerEntity.class);
			success = true;

			// debug
			// Iterator<IPlayer> itPlayers = players.iterator();
			// while (itPlayers.hasNext()) {
			// logger.debug("Created player: " + itPlayers.next().toString());
			// }

			logger.debug("Created player " + player.getName());

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
