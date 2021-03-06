
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Team;

public interface TeamRepository extends CrudRepository<Team, Integer> {
	@Query("SELECT COUNT(t)>0 FROM Team t WHERE t.id = ?1 AND (SELECT u FROM User u WHERE u.username = ?2) MEMBER OF t.members")
	boolean findIsMemberOfTeamByTeamIdAndUsername(int teamId, String username);

	@Query("SELECT COUNT(t)>0 FROM Team t WHERE t.jam.id = ?1 AND (SELECT u FROM User u WHERE u.username = ?2) MEMBER OF t.members")
	boolean findIsMemberOfTeamByJamIdAndUsername(int jamId, String username) throws DataAccessException;

	@Query("SELECT j.teams FROM Jam j WHERE j.id = ?1")
	Collection<Team> findTeamsByJamId(int jamId) throws DataAccessException;
}
