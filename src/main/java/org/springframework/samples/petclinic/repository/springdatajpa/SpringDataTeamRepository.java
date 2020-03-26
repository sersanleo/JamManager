package org.springframework.samples.petclinic.repository.springdatajpa;

import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.repository.TeamRepository;

public interface SpringDataTeamRepository extends TeamRepository, Repository<Team, Integer> {

}
