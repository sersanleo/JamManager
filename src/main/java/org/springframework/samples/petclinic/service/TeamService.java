
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamService {

	@Autowired
	private TeamRepository teamRepository;

	@Transactional(readOnly = true)
	public Team findTeamById(final int id) {
		return this.teamRepository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public boolean findIsMemberOfTeamByJamIdAndUsername(final int jamId, final String username)
			throws DataAccessException {
		return this.teamRepository.findIsMemberOfTeamByJamIdAndUsername(jamId, username);
	}

	@Transactional(readOnly = true)
	public boolean findIsMemberOfTeamByTeamIdAndUsername(final int teamId, final String username)
			throws DataAccessException {
		return this.teamRepository.findIsMemberOfTeamByTeamIdAndUsername(teamId, username);
	}

	@Transactional(readOnly = true)
	public Collection<Team> findTeamsByJamId(int jamId)
			throws DataAccessException {
		return this.teamRepository.findTeamsByJamId(jamId);
	}

	@Transactional
	public void saveTeam(final Team team) throws DataAccessException {
		if (team.getMembers().size() > 0) {
			this.teamRepository.save(team);
		} else {
			this.deleteTeam(team);
		}
	}

	@Transactional
	public void deleteTeam(final Team team) throws DataAccessException {
		this.teamRepository.delete(team);
	}

}
