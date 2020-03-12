package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Team;

public interface TeamRepository extends CrudRepository<Team, Integer>  {

	Team findById(int id) throws DataAccessException;

	Collection<Team> findAll() throws DataAccessException;

	void save(Team team) throws DataAccessException;
	
}
