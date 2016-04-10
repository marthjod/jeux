package de.fhb.jeux.session;

import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.persistence.ShowdownGroup;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.jboss.logging.Logger;

@Stateless

public class CreateGroupBean implements CreateGroupRemote, CreateGroupLocal {

    protected static Logger logger = Logger.getLogger(CreateGroupBean.class);

    @EJB
    private GroupDAO groupDAO;

    public CreateGroupBean() {
    }

    @Override
    public int createGroup(GroupDTO groupDTO) {

        boolean checkOK = false;
        int id = -1;

        // min sets must be <= max sets;
        // creating "completed" groups is OK, because we may import intermediate
        // states
        if (groupDTO.getMinSets() <= groupDTO.getMaxSets()) {
            checkOK = true;
        } else {
            logger.warn("Min sets not <= max sets (" + groupDTO.getMinSets()
                    + ", " + groupDTO.getMaxSets() + ")");
        }

        if (checkOK) {
            // persist new Group entity after having
            // converted it from DTO
            IGroup newGroup = new ShowdownGroup(groupDTO);
            logger.debug("From DTO: " + newGroup);
            groupDAO.addGroup(newGroup);
            logger.debug("Added " + newGroup);
            id = newGroup.getId();
        }

        return id;
    }
}
