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


	@Transactional
	public Team findTeamById(final int id) {
		return this.teamRepository.findById(id);
	}

	@Transactional
	public Collection<Team> findTeams() {
		return this.teamRepository.findAll();
	}

	@Transactional
	public void saveTeam(final Team team) {
		this.teamRepository.save(team);
	}

}
