package de.fhb.jeux.session;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import de.fhb.jeux.dao.GroupDAO;
import de.fhb.jeux.model.IGroup;
import de.fhb.jeux.model.IPlayer;

@Stateless
public class GroupBean implements GroupRemote, GroupLocal {

	@EJB
	private GroupDAO groupDAO;

	@EJB
	private PlayerLocal playerBean;

	public GroupBean() {
	}

	@Override
	public List<IGroup> getAllGroups() {
		return groupDAO.getAllGroups();
	}

	@Override
	public IGroup getGroupById(int groupId) {
		return groupDAO.getGroupById(groupId);
	}

	@Override
	public List<IPlayer> getPlayersInGroup(IGroup group) {
		List<IPlayer> allPlayers = playerBean.getAllPlayers();
		List<IPlayer> groupPlayers = new ArrayList<IPlayer>();

		for (IPlayer player : allPlayers) {
			if (player.getGroup().equals(group)) {
				groupPlayers.add(player);
			}
		}

		return groupPlayers;
	}

	@Override
	public int getGroupSize(IGroup group) {
		return getPlayersInGroup(group).size();
	}
}
