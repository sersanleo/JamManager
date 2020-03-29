package org.springframework.samples.petclinic.repository.springdatajpa;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.model.InvitationStatus;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.InvitationRepository;

public interface SpringDataInvitationRepository extends InvitationRepository, Repository<Invitation, Integer>{

	@Override
	@Query("SELECT invitation FROM Invitation invitation WHERE invitation.from =: team")
	Collection<Invitation> findAllByFrom(@Param("team") Team from);
	
	@Override
	@Query("SELECT invitation FROM Invitation invitation WHERE invitation.to =: user")
	Collection<Invitation> findAllByTo(@Param("to") User to);
	
	@Query("SELECT invitation FROM Invitation invitation WHERE invitation.from =: team AND invitation.to = user AND invitation.status = pending")
	Collection<Invitation> findPendingInvitationByFromAndTo(@Param("from") Team from, @Param("to") User to) throws DataAccessException;
}
