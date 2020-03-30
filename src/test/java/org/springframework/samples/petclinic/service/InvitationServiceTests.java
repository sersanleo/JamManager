package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class InvitationServiceTests {

	@Autowired
	protected InvitationService invitationService;

	@Autowired
	protected UserService userService;

	@Autowired
	protected TeamService teamService;

	@Test
	void shouldFindInvitationById() {
		Invitation invitation = this.invitationService.findInvitationById(1);
		Assertions.assertThat(invitation).isNotEqualTo(null);
	}

	@Test
	void shouldNotFindInvitationById() {
		Invitation invitation = this.invitationService.findInvitationById(20);
		Assertions.assertThat(invitation).isEqualTo(null);
	}

	@Test
	void shouldFindPendingInvitationsByUsername() {
		Assertions.assertThat(this.invitationService.findPendingInvitationsByUsername("member1").size()).isEqualTo(2);
	}

	@Test
	void shouldFindPendingInvitationsByJamIdAndUsername() {
		Assertions.assertThat(this.invitationService.findPendingInvitationsByJamIdAndUsername(1, "member1").size())
				.isEqualTo(2);
	}

	@Test
	void shouldFindHasPendingInvitationsByTeamIdAndUsername() {
		Assertions.assertThat(this.invitationService.findHasPendingInvitationsByTeamIdAndUsername(1, "member3"))
				.isEqualTo(true);
		Assertions.assertThat(this.invitationService.findHasPendingInvitationsByTeamIdAndUsername(10, "member3"))
				.isEqualTo(false);
	}

	@Test
	@Transactional
	public void shouldInsertInvitationAndGenerateId() {
		Collection<Invitation> invitations = this.invitationService.findPendingInvitationsByUsername("member2");
		int found = invitations.size();

		Invitation invitation = new Invitation();
		invitation.setTo(this.userService.findByUsername("member2"));
		invitation.setFrom(this.teamService.findTeamById(12));

		this.invitationService.saveInvitation(invitation);
		Assertions.assertThat(invitation.getId()).isNotNull();

		invitations = this.invitationService.findPendingInvitationsByUsername("member2");
		Assertions.assertThat(invitations.size()).isEqualTo(found + 1);
	}

	@Test
	@Transactional
	public void shouldDeleteInvitation() {
		Invitation invitation = this.invitationService.findInvitationById(2);
		Assertions.assertThat(invitation).isNotNull();
		this.invitationService.deleteInvitation(invitation);
		Assertions.assertThat(this.invitationService.findInvitationById(2)).isNull();

	}

	@Test
	@Transactional
	public void shouldDeleteAllPendingInvitationsByJamIdAndUsername() {
		Assertions.assertThat(this.invitationService.findPendingInvitationsByUsername("member1").size())
				.isGreaterThan(0);

		this.invitationService.deleteAllPendingInvitationsByJamIdAndUsername(1, "member1");

		Assertions.assertThat(this.invitationService.findPendingInvitationsByUsername("member1").size()).isEqualTo(0);
	}
}
