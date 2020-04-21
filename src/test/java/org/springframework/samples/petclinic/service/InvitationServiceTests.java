package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.NoSuchElementException;

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
	void shouldNotFindInvitationByInexistentId() {
		Assertions.assertThatThrownBy(() -> this.invitationService.findInvitationById(200))
				.isInstanceOf(NoSuchElementException.class);
	}

	@Test
	void shouldFindPendingInvitationsByUsername() {
		Assertions.assertThat(this.invitationService.findPendingInvitationsByUsername("member1").size()).isEqualTo(2);
	}

	@Test
	void shouldNotFindPendingInvitationsByInexistentUsername() {
		Assertions.assertThat(this.invitationService.findPendingInvitationsByUsername("nonExistentUser").size())
				.isEqualTo(0);
	}

	@Test
	void shouldFindPendingInvitationsByJamIdAndUsername() {
		Assertions.assertThat(this.invitationService.findPendingInvitationsByJamIdAndUsername(1, "member1").size())
				.isEqualTo(2);
	}

	@Test
	void shouldFindHasPendingInvitationsByTeamIdAndUsername() {
		Assertions.assertThat(this.invitationService.findHasPendingInvitationsByTeamIdAndUsername(1, "member4"))
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
	public void shouldNotInsertNullInvitation() {
		Assertions.assertThatThrownBy(() -> this.invitationService.saveInvitation(null)).isInstanceOf(Exception.class);
	}

	@Test
	@Transactional
	public void shouldDeleteInvitation() {
		Invitation invitation = this.invitationService.findInvitationById(2);
		Assertions.assertThat(invitation).isNotNull();

		this.invitationService.deleteInvitation(invitation);
		Assertions.assertThatThrownBy(() -> this.invitationService.findInvitationById(2))
				.isInstanceOf(NoSuchElementException.class);
	}

	@Test
	public void shouldNotDeleteNullInvitation() {
		Assertions.assertThatThrownBy(() -> this.invitationService.deleteInvitation(null)).isInstanceOf(Exception.class);
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
