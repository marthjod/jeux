package de.fhb.jeux.session;

import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import com.google.gson.Gson;

import de.fhb.jeux.mockentity.MockGroupEntity;
import de.fhb.jeux.model.IGroup;

@Stateless
public class CreateNewGroupBean implements CreateNewGroupRemote,
		CreateNewGroupLocal {

	protected static Logger logger = Logger.getLogger(CreateNewGroupBean.class);

	private final Gson gson;

	public CreateNewGroupBean() {
		this.gson = new Gson();
	}

	@Override
	public boolean createNewGroup(String jsonRepresentation) {
		boolean success = false;

		// TODO try/catch exception from Gson here
		IGroup group = gson.fromJson(jsonRepresentation.toString(),
				MockGroupEntity.class);

		// if (success) {
		logger.debug("Created group " + group.toString());
		// } else {
		// logger.debug("Failed to create group from JSON '"
		// + jsonRepresentation + "'");
		// }

		return success;
	}
}
