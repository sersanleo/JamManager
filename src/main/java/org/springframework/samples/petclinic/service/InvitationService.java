
package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.repository.InvitationRepository;
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
	public Collection<Invitation> findPendingInvitationsByUsername(final String username) {
		return this.invitationRepository.findPendingInvitationsByUsername(username);
	}

	@Transactional
	public Collection<Invitation> findPendingInvitationsByJamIdAndUsername(final int jamId, final String username) {
		return this.invitationRepository.findPendingInvitationsByJamIdAndUsername(jamId, username);
	}

	@Transactional
	public void saveInvitation(final Invitation invitation) {
		this.invitationRepository.save(invitation);
	}

	@Transactional
	public void deleteInvitation(final Invitation invitation) {
		this.invitationRepository.delete(invitation);
	}

	public void deleteAllPendingInvitationsByJamIdAndUsername(final int jamId, final String username) {
		// this.invitationRepository.deleteAllPendingInvitationsByJamIdAndUsername(jamId,
		// username); (no funciona)

		for (Invitation i : this.findPendingInvitationsByJamIdAndUsername(jamId, username)) {
			this.deleteInvitation(i);
		}
	}
}
