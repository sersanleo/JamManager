package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.model.InvitationStatus;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class InvitationControllerE2ETests {
	private static final int TEST_JAM_ID = 1;
	private static final int TEST_TEAM_ID = 1;
	private static final int TEST_NONEXISTENT_TEAM_ID = 100;
	private static final int TEST_PENDING_INVITATION_ID = 1;
	private static final int TEST_NONEXISTENT_INVITATION_ID = 100;

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testListUserInvitations() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/invitations"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("invitations/invitationList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("invitations"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testSuccessfulInitInvitationCreationForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/invitations/new",
						InvitationControllerE2ETests.TEST_JAM_ID, InvitationControllerE2ETests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("invitation"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInitInvitationCreationFormFullTeam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/invitations/new",
						InvitationControllerE2ETests.TEST_JAM_ID, InvitationControllerE2ETests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInitInvitationCreationFormNonExistentTeam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/invitations/new",
						InvitationControllerE2ETests.TEST_JAM_ID, InvitationControllerE2ETests.TEST_NONEXISTENT_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInitInvitationCreationFormByNonMemberOfTheTeam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/invitations/new",
						InvitationControllerE2ETests.TEST_JAM_ID, InvitationControllerE2ETests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInitInvitationCreationFormJamNonInscription() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/invitations/new",
						InvitationControllerE2ETests.TEST_JAM_ID, InvitationControllerE2ETests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testSuccesfulInvitationCreation() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerE2ETests.TEST_JAM_ID,
								InvitationControllerE2ETests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "member5"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInvitationCreationFullTeam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerE2ETests.TEST_JAM_ID,
								InvitationControllerE2ETests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "testUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInvitationCreationNonExistentTeam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerE2ETests.TEST_JAM_ID,
								InvitationControllerE2ETests.TEST_NONEXISTENT_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "testUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInvitationCreationJamNonInscription() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerE2ETests.TEST_JAM_ID,
								InvitationControllerE2ETests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "testUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInvitationCreationByNonMember() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerE2ETests.TEST_JAM_ID,
								InvitationControllerE2ETests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "testUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testFailedInvitationCreationInexistentUser() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerE2ETests.TEST_JAM_ID,
								InvitationControllerE2ETests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "inexistentUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("invitation"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("invitation", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("invitation", "to.username"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("invitation", "to.username",
						"wrongUser"))
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testFailedInvitationCreationUsernameInTeam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerE2ETests.TEST_JAM_ID,
								InvitationControllerE2ETests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "member2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("invitation"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("invitation", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("invitation", "to.username"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("invitation", "to.username",
						"isMember"))
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testFailedInvitationCreationUsernameInJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerE2ETests.TEST_JAM_ID,
								InvitationControllerE2ETests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "member3"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("invitation"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("invitation", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("invitation", "to.username"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("invitation", "to.username",
						"isParticipating"))
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testFailedInvitationCreationPendingInvitation() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerE2ETests.TEST_JAM_ID,
								InvitationControllerE2ETests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "member4"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("invitation"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("invitation", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("invitation", "to.username"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("invitation", "to.username",
						"pendingInvitation"))
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testFailedInvitationCreationNotMemberAuthority() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerE2ETests.TEST_JAM_ID,
								InvitationControllerE2ETests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "judge1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("invitation"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("invitation", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("invitation", "to.username"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("invitation", "to.username",
						"notMember"))
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testSuccesfulInvitationRemove() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete",
								InvitationControllerE2ETests.TEST_JAM_ID, InvitationControllerE2ETests.TEST_TEAM_ID,
								InvitationControllerE2ETests.TEST_PENDING_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInvitationRemoveNonExistentInvitation() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete",
								InvitationControllerE2ETests.TEST_JAM_ID, InvitationControllerE2ETests.TEST_TEAM_ID,
								InvitationControllerE2ETests.TEST_NONEXISTENT_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInvitationRemoveInvitationNotPending() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete",
								InvitationControllerE2ETests.TEST_JAM_ID, InvitationControllerE2ETests.TEST_TEAM_ID,
								InvitationControllerE2ETests.TEST_PENDING_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInvitationRemoveInvitationByNonMember() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete",
								InvitationControllerE2ETests.TEST_JAM_ID, InvitationControllerE2ETests.TEST_TEAM_ID,
								InvitationControllerE2ETests.TEST_PENDING_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInvitationRemoveInvitationJamNonInscription() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete",
								InvitationControllerE2ETests.TEST_JAM_ID, InvitationControllerE2ETests.TEST_TEAM_ID,
								InvitationControllerE2ETests.TEST_PENDING_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testSuccesfulInvitationAccept() throws Exception {
		this.mockMvc
		.perform(MockMvcRequestBuilders
				.get("/invitations/{invitationId}/accept", InvitationControllerE2ETests.TEST_PENDING_INVITATION_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf()))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInvitationAcceptNonExistent() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/accept",
								InvitationControllerE2ETests.TEST_NONEXISTENT_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member4", authorities = { "member" })
	@ParameterizedTest
	@EnumSource(value = InvitationStatus.class, names = { "ACCEPTED", "REJECTED" })
	void testFailedInvitationAcceptNotPending(final InvitationStatus status) throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/accept", 4)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member5", authorities = { "member" })
	@Test
	void testFailedInvitationAcceptNotForYou() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/accept", InvitationControllerE2ETests.TEST_PENDING_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInvitationAcceptJamNotInInscription() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/accept", 7)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testSuccesfulInvitationReject() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/reject", InvitationControllerE2ETests.TEST_PENDING_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/invitations"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInvitationRejectNonExistent() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/reject",
								InvitationControllerE2ETests.TEST_NONEXISTENT_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member4", authorities = { "member" })
	@ParameterizedTest
	@EnumSource(value = InvitationStatus.class, names = { "ACCEPTED", "REJECTED" })
	void testFailedInvitationRejectNotPending(final InvitationStatus status) throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/reject", 4)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member5", authorities = { "member" })
	@Test
	void testFailedInvitationRejectNotForYou() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/reject", InvitationControllerE2ETests.TEST_PENDING_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInvitationRejectJamNotInInscription() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/reject", 7)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
}