package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Delivery;
import org.springframework.samples.petclinic.model.Team;

public interface DeliveryRepository extends CrudRepository<Delivery, Integer> {

	@Query("SELECT d FROM Delivery d WHERE d.team = ?1")
	Collection<Delivery> findDeliverysByTeam(Team team) throws DataAccessException;
}
