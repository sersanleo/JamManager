package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;


public interface UserRepository extends  CrudRepository<User, String>{
	Collection<User> findByUsername(String username) throws DataAccessException;
	
	User findOnlyByUsername(String username) throws DataAccessException;

	@Override
	Collection<User> findAll() throws DataAccessException;
}
