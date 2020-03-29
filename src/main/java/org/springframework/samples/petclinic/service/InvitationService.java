package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.JamResource;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.repository.InvitationRepository;
import org.springframework.samples.petclinic.repository.JamResourceRepository;
import org.springframework.samples.petclinic.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvitationService {
	
	@Autowired
	private InvitationRepository invitationRepository;

	@Autowired
	public InvitationService(InvitationRepository invitationRepository) {
		this.invitationRepository = invitationRepository;
	}

	@Transactional(readOnly = true)
	public Invitation findInvitationById(int id)  throws DataAccessException {
		return invitationRepository.findById(id);
	}

	@Transactional
	public Collection<Invitation> findInvitations() {
		return this.invitationRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Collection<Invitation> findInvitationsByTeam(Team team) throws DataAccessException {
		return this.invitationRepository.findAllByFrom(team);
	}

	@Transactional(readOnly = true)
	public Collection<Invitation> findInvitationsByUser(User user) throws DataAccessException {
		return this.invitationRepository.findAllByTo(user);
	}
	
	@Transactional
	public void saveInvitation(final Invitation invitation) {
		this.invitationRepository.save(invitation);
	}
	
	@Transactional
	public void deleteInvitation(Invitation invitation) throws DataAccessException{
		invitationRepository.delete(invitation);
	}
	
	@Transactional (readOnly =true)
	public Collection<Invitation> findPendingInvitationsByTeamAndUser(Team team, User user) throws DataAccessException{
		return invitationRepository.findPendingInvitationByFromAndTo(team, user);
	}
}
