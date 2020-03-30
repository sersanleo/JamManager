package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.User;

public interface UserRepository extends CrudRepository<User, String> {
	User findByUsername(String username) throws DataAccessException;

	@Override
	Collection<User> findAll() throws DataAccessException;
}
