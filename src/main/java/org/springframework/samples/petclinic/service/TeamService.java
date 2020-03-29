
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.JamResource;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TeamService {

	@Autowired
	private TeamRepository teamRepository;

	@Transactional(readOnly = true)
	public Team findTeamById(final int id) throws DataAccessException {
		return this.teamRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public boolean findIsMemberOfTeamByJamIdAndUsername(final int jamId, final String username)
			throws DataAccessException {
		return this.teamRepository.findIsMemberOfTeamByJamIdAndUsername(jamId, username);
	}

	@Transactional(readOnly = true)
	public boolean findIsMemberOfTeamByTeamIdAndUsername(final int teamId, final String username) throws DataAccessException {
		return this.teamRepository.findIsMemberOfTeamByTeamIdAndUsername(teamId, username);
	}

	@Transactional
	public void saveTeam(final Team team) throws DataAccessException {
		this.teamRepository.save(team);
	}
	
	@Transactional
	public void deleteTeam(Team team) throws DataAccessException{
		teamRepository.delete(team);
	}

}
