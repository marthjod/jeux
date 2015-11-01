package de.fhb.jeux.session;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import org.jboss.logging.Logger;

import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.dto.GroupDTO;
import de.fhb.jeux.dto.PlayerDTO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;
import de.fhb.jeux.persistence.ShowdownPlayer;

@Stateless

public class GroupBean implements GroupRemote, GroupLocal {

    protected static Logger logger = Logger.getLogger(GroupBean.class);

    @EJB
    private GroupDAO groupDAO;

    public GroupBean() {
    }

    @Override
    public List<GroupDTO> getAllGroupDTOs() {
        return groupDAO.getAllGroupDTOs();
    }

    @Override
    public IGroup getGroupById(int groupId) {
        return groupDAO.getGroupById(groupId);
    }

    @Override
    public IGroup getGroupByName(String name) {
        return groupDAO.getGroupByName(name);
    }

    private List<ShowdownPlayer> getPlayersInGroup(IGroup group) {
        return groupDAO.getPlayersInGroup(group);
    }

    @Override
    public List<PlayerDTO> getPlayerDTOsInGroup(IGroup group) {
        List<PlayerDTO> playerDTOs = new ArrayList<PlayerDTO>();
        if (group != null) {
            for (IPlayer player : getPlayersInGroup(group)) {
                playerDTOs.add(new PlayerDTO(player));
            }
        }
        return playerDTOs;
    }
}
