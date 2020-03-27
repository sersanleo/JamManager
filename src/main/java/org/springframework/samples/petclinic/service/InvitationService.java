package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.repository.InvitationRepository;
import org.springframework.samples.petclinic.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InvitationService {
	
	@Autowired
	private InvitationRepository invitationRepository;


	@Transactional
	public Invitation findInvitationById(final int id) {
		return this.invitationRepository.findById(id);
	}

	@Transactional
	public Collection<Invitation> findInvitations() {
		return this.invitationRepository.findAll();
	}

	@Transactional
	public void saveInvitation(final Invitation invitation) {
		this.invitationRepository.save(invitation);
	}
}
