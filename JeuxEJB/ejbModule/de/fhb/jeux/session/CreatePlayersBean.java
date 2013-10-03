package de.fhb.jeux.session;

import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import de.fhb.jeux.mockentity.MockPlayerEntity;
import de.fhb.jeux.model.IPlayer;

@Stateless
public class CreatePlayersBean implements CreatePlayersRemote,
		CreatePlayersLocal {

	protected static Logger logger = Logger.getLogger(CreatePlayersBean.class);

	private Gson gson;

	public CreatePlayersBean() {
		// this.gson = new GsonBuilder().registerTypeAdapter(IPlayer.class,
		// new IPlayerAdapter().nullSafe()).create();
		this.gson = new GsonBuilder().create();
	}

	@Override
	public boolean createPlayers(String jsonRepresentation) {
		boolean success = false;

		List<IPlayer> players;

		try {
			// http://stackoverflow.com/questions/4318458/how-to-deserialize-a-list-using-gson-or-another-json-to-java
			players = gson.fromJson(jsonRepresentation,
					new TypeToken<List<MockPlayerEntity>>() {
					}.getType());
			success = true;

			// debug
			Iterator<IPlayer> itPlayers = players.iterator();
			while (itPlayers.hasNext()) {
				logger.debug("Created player: " + itPlayers.next().toString());
			}

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
			logger.error("Failed to create players from JSON input '"
					+ jsonRepresentation + "'");
		}
		return success;
	}
}
