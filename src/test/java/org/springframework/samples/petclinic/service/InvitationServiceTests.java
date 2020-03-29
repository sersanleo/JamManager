package org.springframework.samples.petclinic.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.model.InvitationStatus;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.util.EntityUtils;
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
		Invitation invitation = this.invitationService.findInvitationById(10);
		Assertions.assertThat(invitation).isEqualTo(null);
	}

	@Test
	public void shouldSaveInvitation() {
		Invitation invitation = new Invitation();
		Team team = this.teamService.findTeamById(1);
		User user = this.userService.findOnlyByUsername("member5");

		invitation.setCreationDate(LocalDateTime.now().minusNanos(1));
		invitation.setFrom(team);
		invitation.setTo(user);
		invitation.setStatus(InvitationStatus.PENDING);

		this.invitationService.saveInvitation(invitation);
		Assertions.assertThat(invitation.getId()).isNotNull();
	}

	@Test
	public void shouldFindPendingInvitationsByTeamAndUser() {
		User user = this.userService.findOnlyByUsername("member4");
		User user2 = this.userService.findOnlyByUsername("member5");
		Team team = this.teamService.findTeamById(1);

		Collection<Invitation> invitation = this.invitationService.findPendingInvitationsByTeamAndUser(team, user);
		Collection<Invitation> notinvitation = this.invitationService.findPendingInvitationsByTeamAndUser(team, user2);

		Assertions.assertThat(invitation.size()).isEqualTo(1);
		Assertions.assertThat(notinvitation.size()).isEqualTo(0);
	}

	@Test
	public void shouldDeleteInvitation() {
		Invitation invitation = this.invitationService.findInvitationById(1);
		Assertions.assertThat(invitation).isNotNull();
		this.invitationService.deleteInvitation(invitation);
		Assertions.assertThat(this.invitationService.findInvitationById(1)).isNull();

	}
}
