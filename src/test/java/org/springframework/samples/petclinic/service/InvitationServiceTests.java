package org.springframework.samples.petclinic.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.stereotype.Service;

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
	public void shouldDeleteInvitation() {
		Invitation invitation = this.invitationService.findInvitationById(1);
		Assertions.assertThat(invitation).isNotNull();
		this.invitationService.deleteInvitation(invitation);
		Assertions.assertThat(this.invitationService.findInvitationById(1)).isNull();

	}
}
