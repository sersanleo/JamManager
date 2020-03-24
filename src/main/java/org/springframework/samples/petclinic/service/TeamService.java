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
	
	private TeamRepository teamRepository;


	@Autowired
	public TeamService(final TeamRepository teamRepository) {
		this.teamRepository = teamRepository;
	}

	@Transactional(readOnly = true)
	public Team findTeamById(final int id) throws DataAccessException {
		return this.teamRepository.findById(id);
	}

	@Transactional(readOnly = true)
	public Collection<Team> findTeams() throws DataAccessException {
		return this.teamRepository.findAll();
	}

	@Transactional
	public void saveTeam(final Team team) throws DataAccessException {
		this.teamRepository.save(team);
	}

}
