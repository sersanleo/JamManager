
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Jam;

public interface JamRepository extends CrudRepository<Jam, Integer> {
	@Override
	Collection<Jam> findAll() throws DataAccessException;

}
