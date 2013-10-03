package de.fhb.jeux.json;

import java.io.IOException;

import org.jboss.logging.Logger;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import de.fhb.jeux.mockentity.MockPlayerEntity;
import de.fhb.jeux.model.IPlayer;

// http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/TypeAdapter.html
// http://google-gson.googlecode.com/svn/trunk/gson/docs/javadocs/com/google/gson/stream/JsonReader.html

public class IPlayerAdapter extends TypeAdapter<IPlayer> {

	protected static Logger logger = Logger.getLogger(IPlayerAdapter.class);

	@Override
	public IPlayer read(JsonReader reader) throws IOException {

		IPlayer player = new MockPlayerEntity();

		try {
			if (reader.peek() == JsonToken.NULL) {
				reader.nextNull();
			} else {
				reader.beginObject();
				if (reader.peek() == JsonToken.NAME
						&& "name".equals(reader.nextName())
						&& reader.peek() == JsonToken.STRING) {

					String name = reader.nextString();
					player = new MockPlayerEntity(name);

				}
			}
			reader.endObject();
		} catch (IOException e) {
			logger.error("I/O exception during JsonReader.*");
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.error("I/O exception while closing JsonReader");
			}
		}

		logger.debug("returning " + player.toString());
		return player;
	}

	@Override
	public void write(JsonWriter arg0, IPlayer arg1) throws IOException {
		// TODO
	}
}