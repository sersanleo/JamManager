package org.springframework.samples.petclinic.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Mark;

public interface MarkRepository extends CrudRepository<Mark, Integer> {
	@Query("SELECT m FROM Mark m WHERE m.team.id = ?1 AND m.judge.username = ?2")
	Optional<Mark> findByTeamIdAndJudgeUsername(int teamId, String judgeUsername);

}
