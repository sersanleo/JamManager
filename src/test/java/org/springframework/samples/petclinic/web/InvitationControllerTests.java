package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.model.InvitationStatus;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.InvitationService;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = InvitationController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class InvitationControllerTests {
	private static final int TEST_JAM_ID = 1;
	private static final int TEST_TEAM_ID = 1;
	private static final int TEST_NONEXISTENT_TEAM_ID = 100;
	private static final int TEST_INVITATION_ID = 1;
	private static final int TEST_NONEXISTENT_INVITATION_ID = 100;

	@MockBean
	private InvitationService invitationService;
	@MockBean
	private TeamService teamService;
	@MockBean
	private UserService userService;
	@MockBean
	private AuthoritiesService authoritiesService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	private void beforeEach() {
		BDDMockito.when(this.teamService.findTeamById(InvitationControllerTests.TEST_NONEXISTENT_TEAM_ID))
				.thenThrow(NoSuchElementException.class);
		BDDMockito.when(this.invitationService.findInvitationById(InvitationControllerTests.TEST_NONEXISTENT_INVITATION_ID))
				.thenThrow(NoSuchElementException.class);
	}

	@WithMockUser(value = "spring")
	@Test
	void testListUserInvitations() throws Exception {
		BDDMockito.when(this.invitationService.findPendingInvitationsByUsername("spring")).thenReturn(new HashSet());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/invitations"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("invitations/invitationList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("invitations"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccessfulInitInvitationCreationForm() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setJam(jam);

		BDDMockito.when(this.teamService.findTeamById(InvitationControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/invitations/new",
						InvitationControllerTests.TEST_JAM_ID, InvitationControllerTests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("invitation"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInitInvitationCreationFormNonExistentTeam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/invitations/new",
						InvitationControllerTests.TEST_JAM_ID, InvitationControllerTests.TEST_NONEXISTENT_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInitInvitationCreationFormByNonMemberOfTheTeam() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setJam(jam);

		BDDMockito.when(this.teamService.findTeamById(InvitationControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(false);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/invitations/new",
						InvitationControllerTests.TEST_JAM_ID, InvitationControllerTests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInitInvitationCreationFormJamNonInscription() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(1));
		team.setJam(jam);

		BDDMockito.when(this.teamService.findTeamById(InvitationControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/invitations/new",
						InvitationControllerTests.TEST_JAM_ID, InvitationControllerTests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulInvitationCreation() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setJam(jam);
		User user = new User();
		user.setUsername("testUser");

		BDDMockito.when(this.teamService.findTeamById(InvitationControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);

		BDDMockito.when(this.userService.findByUsername("testUser")).thenReturn(user);
		BDDMockito.when(this.invitationService
				.findHasPendingInvitationsByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID, "testUser"))
				.thenReturn(false);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"testUser")).thenReturn(false);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByJamIdAndUsername(InvitationControllerTests.TEST_JAM_ID,
				"testUser")).thenReturn(false);
		BDDMockito.when(this.authoritiesService.findHasAuthorityByUsername("testUser", "member")).thenReturn(true);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerTests.TEST_JAM_ID,
								InvitationControllerTests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "testUser"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationCreationNonExistentTeam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerTests.TEST_JAM_ID,
								InvitationControllerTests.TEST_NONEXISTENT_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "testUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationCreationJamNonInscription() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(1));
		team.setJam(jam);
		User user = new User();
		user.setUsername("testUser");

		BDDMockito.when(this.teamService.findTeamById(InvitationControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);

		BDDMockito.when(this.userService.findByUsername("testUser")).thenReturn(user);
		BDDMockito.when(this.invitationService
				.findHasPendingInvitationsByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID, "testUser"))
				.thenReturn(false);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"testUser")).thenReturn(false);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByJamIdAndUsername(InvitationControllerTests.TEST_JAM_ID,
				"testUser")).thenReturn(false);
		BDDMockito.when(this.authoritiesService.findHasAuthorityByUsername("testUser", "member")).thenReturn(true);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerTests.TEST_JAM_ID,
								InvitationControllerTests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "testUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationCreationByNonMember() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setJam(jam);
		User user = new User();
		user.setUsername("testUser");

		BDDMockito.when(this.teamService.findTeamById(InvitationControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(false);

		BDDMockito.when(this.userService.findByUsername("testUser")).thenReturn(user);
		BDDMockito.when(this.invitationService
				.findHasPendingInvitationsByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID, "testUser"))
				.thenReturn(false);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"testUser")).thenReturn(false);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByJamIdAndUsername(InvitationControllerTests.TEST_JAM_ID,
				"testUser")).thenReturn(false);
		BDDMockito.when(this.authoritiesService.findHasAuthorityByUsername("testUser", "member")).thenReturn(true);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerTests.TEST_JAM_ID,
								InvitationControllerTests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "testUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationCreationInexistentUser() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setJam(jam);

		BDDMockito.when(this.teamService.findTeamById(InvitationControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);

		BDDMockito.when(this.userService.findByUsername("testUser")).thenReturn(null);
		BDDMockito.when(this.invitationService
				.findHasPendingInvitationsByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID, "testUser"))
				.thenReturn(false);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"testUser")).thenReturn(false);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByJamIdAndUsername(InvitationControllerTests.TEST_JAM_ID,
				"testUser")).thenReturn(false);
		BDDMockito.when(this.authoritiesService.findHasAuthorityByUsername("testUser", "member")).thenReturn(true);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerTests.TEST_JAM_ID,
								InvitationControllerTests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "testUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("invitation"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("invitation", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("invitation", "to.username"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("invitation", "to.username",
						"wrongUser"))
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationCreationUsernameInTeam() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setJam(jam);
		User user = new User();
		user.setUsername("testUser");

		BDDMockito.when(this.teamService.findTeamById(InvitationControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);

		BDDMockito.when(this.userService.findByUsername("testUser")).thenReturn(user);
		BDDMockito.when(this.invitationService
				.findHasPendingInvitationsByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID, "testUser"))
				.thenReturn(false);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"testUser")).thenReturn(true);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByJamIdAndUsername(InvitationControllerTests.TEST_JAM_ID,
				"testUser")).thenReturn(false);
		BDDMockito.when(this.authoritiesService.findHasAuthorityByUsername("testUser", "member")).thenReturn(true);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerTests.TEST_JAM_ID,
								InvitationControllerTests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "testUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("invitation"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("invitation", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("invitation", "to.username"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("invitation", "to.username",
						"isMember"))
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationCreationUsernameInJam() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setJam(jam);
		User user = new User();
		user.setUsername("testUser");

		BDDMockito.when(this.teamService.findTeamById(InvitationControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);

		BDDMockito.when(this.userService.findByUsername("testUser")).thenReturn(user);
		BDDMockito.when(this.invitationService
				.findHasPendingInvitationsByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID, "testUser"))
				.thenReturn(false);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"testUser")).thenReturn(false);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByJamIdAndUsername(InvitationControllerTests.TEST_JAM_ID,
				"testUser")).thenReturn(true);
		BDDMockito.when(this.authoritiesService.findHasAuthorityByUsername("testUser", "member")).thenReturn(true);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerTests.TEST_JAM_ID,
								InvitationControllerTests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "testUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("invitation"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("invitation", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("invitation", "to.username"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("invitation", "to.username",
						"isParticipating"))
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationCreationPendingInvitation() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setJam(jam);
		User user = new User();
		user.setUsername("testUser");

		BDDMockito.when(this.teamService.findTeamById(InvitationControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);

		BDDMockito.when(this.userService.findByUsername("testUser")).thenReturn(user);
		BDDMockito.when(this.invitationService
				.findHasPendingInvitationsByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID, "testUser"))
				.thenReturn(true);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"testUser")).thenReturn(false);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByJamIdAndUsername(InvitationControllerTests.TEST_JAM_ID,
				"testUser")).thenReturn(false);
		BDDMockito.when(this.authoritiesService.findHasAuthorityByUsername("testUser", "member")).thenReturn(true);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerTests.TEST_JAM_ID,
								InvitationControllerTests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "testUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("invitation"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("invitation", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("invitation", "to.username"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("invitation", "to.username",
						"pendingInvitation"))
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationCreationNotMemberAuthority() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setJam(jam);
		User user = new User();
		user.setUsername("testUser");

		BDDMockito.when(this.teamService.findTeamById(InvitationControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);

		BDDMockito.when(this.userService.findByUsername("testUser")).thenReturn(user);
		BDDMockito.when(this.invitationService
				.findHasPendingInvitationsByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID, "testUser"))
				.thenReturn(false);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"testUser")).thenReturn(false);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByJamIdAndUsername(InvitationControllerTests.TEST_JAM_ID,
				"testUser")).thenReturn(false);
		BDDMockito.when(this.authoritiesService.findHasAuthorityByUsername("testUser", "member")).thenReturn(false);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/invitations/new", InvitationControllerTests.TEST_JAM_ID,
								InvitationControllerTests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "testUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("invitation"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("invitation", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("invitation", "to.username"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("invitation", "to.username",
						"notMember"))
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulInvitationRemove() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setJam(jam);
		User user = new User();
		user.setUsername("testUser");
		Invitation invitation = new Invitation();
		invitation.setId(InvitationControllerTests.TEST_INVITATION_ID);
		invitation.setStatus(InvitationStatus.PENDING);
		invitation.setFrom(team);

		BDDMockito.when(this.invitationService.findInvitationById(InvitationControllerTests.TEST_INVITATION_ID))
				.thenReturn(invitation);

		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete",
								InvitationControllerTests.TEST_JAM_ID, InvitationControllerTests.TEST_TEAM_ID,
								InvitationControllerTests.TEST_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationRemoveNonExistentInvitation() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete",
								InvitationControllerTests.TEST_JAM_ID, InvitationControllerTests.TEST_TEAM_ID,
								InvitationControllerTests.TEST_NONEXISTENT_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationRemoveInvitationNotPending() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setJam(jam);
		User user = new User();
		user.setUsername("testUser");
		Invitation invitation = new Invitation();
		invitation.setId(InvitationControllerTests.TEST_INVITATION_ID);
		invitation.setStatus(InvitationStatus.REJECTED);
		invitation.setFrom(team);

		BDDMockito.when(this.invitationService.findInvitationById(InvitationControllerTests.TEST_INVITATION_ID))
				.thenReturn(invitation);

		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete",
								InvitationControllerTests.TEST_JAM_ID, InvitationControllerTests.TEST_TEAM_ID,
								InvitationControllerTests.TEST_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationRemoveInvitationByNonMember() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setJam(jam);
		User user = new User();
		user.setUsername("testUser");
		Invitation invitation = new Invitation();
		invitation.setId(InvitationControllerTests.TEST_INVITATION_ID);
		invitation.setStatus(InvitationStatus.PENDING);
		invitation.setFrom(team);

		BDDMockito.when(this.invitationService.findInvitationById(InvitationControllerTests.TEST_INVITATION_ID))
				.thenReturn(invitation);

		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(false);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete",
								InvitationControllerTests.TEST_JAM_ID, InvitationControllerTests.TEST_TEAM_ID,
								InvitationControllerTests.TEST_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationRemoveInvitationJamNonInscription() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(1));
		team.setJam(jam);
		User user = new User();
		user.setUsername("testUser");
		Invitation invitation = new Invitation();
		invitation.setId(InvitationControllerTests.TEST_INVITATION_ID);
		invitation.setStatus(InvitationStatus.PENDING);
		invitation.setFrom(team);

		BDDMockito.when(this.invitationService.findInvitationById(InvitationControllerTests.TEST_INVITATION_ID))
				.thenReturn(invitation);

		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete",
								InvitationControllerTests.TEST_JAM_ID, InvitationControllerTests.TEST_TEAM_ID,
								InvitationControllerTests.TEST_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulInvitationAccept() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setMembers(new HashSet());
		team.setJam(jam);
		User user = new User();
		user.setUsername("spring");
		Invitation invitation = new Invitation();
		invitation.setId(InvitationControllerTests.TEST_INVITATION_ID);
		invitation.setFrom(team);
		invitation.setTo(user);

		BDDMockito.when(this.invitationService.findInvitationById(InvitationControllerTests.TEST_INVITATION_ID))
				.thenReturn(invitation);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/accept", InvitationControllerTests.TEST_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationAcceptNonExistent() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/accept",
								InvitationControllerTests.TEST_NONEXISTENT_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@ParameterizedTest
	@EnumSource(value = InvitationStatus.class, names = { "ACCEPTED", "REJECTED" })
	void testFailedInvitationAcceptNotPending(final InvitationStatus status) throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setMembers(new HashSet());
		team.setJam(jam);
		User user = new User();
		user.setUsername("spring");
		Invitation invitation = new Invitation();
		invitation.setId(InvitationControllerTests.TEST_INVITATION_ID);
		invitation.setStatus(status);
		invitation.setFrom(team);
		invitation.setTo(user);

		BDDMockito.when(this.invitationService.findInvitationById(InvitationControllerTests.TEST_INVITATION_ID))
				.thenReturn(invitation);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/accept", InvitationControllerTests.TEST_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationAcceptNotForYou() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setMembers(new HashSet());
		team.setJam(jam);
		User user = new User();
		user.setUsername("notSpring");
		Invitation invitation = new Invitation();
		invitation.setId(InvitationControllerTests.TEST_INVITATION_ID);
		invitation.setFrom(team);
		invitation.setTo(user);

		BDDMockito.when(this.invitationService.findInvitationById(InvitationControllerTests.TEST_INVITATION_ID))
				.thenReturn(invitation);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/accept", InvitationControllerTests.TEST_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationAcceptJamNotInInscription() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(1));
		team.setMembers(new HashSet());
		team.setJam(jam);
		User user = new User();
		user.setUsername("spring");
		Invitation invitation = new Invitation();
		invitation.setId(InvitationControllerTests.TEST_INVITATION_ID);
		invitation.setFrom(team);
		invitation.setTo(user);

		BDDMockito.when(this.invitationService.findInvitationById(InvitationControllerTests.TEST_INVITATION_ID))
				.thenReturn(invitation);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/accept", InvitationControllerTests.TEST_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulInvitationReject() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setMembers(new HashSet());
		team.setJam(jam);
		User user = new User();
		user.setUsername("spring");
		Invitation invitation = new Invitation();
		invitation.setId(InvitationControllerTests.TEST_INVITATION_ID);
		invitation.setFrom(team);
		invitation.setTo(user);

		BDDMockito.when(this.invitationService.findInvitationById(InvitationControllerTests.TEST_INVITATION_ID))
				.thenReturn(invitation);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/reject", InvitationControllerTests.TEST_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/invitations"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationRejectNonExistent() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/reject",
								InvitationControllerTests.TEST_NONEXISTENT_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@ParameterizedTest
	@EnumSource(value = InvitationStatus.class, names = { "ACCEPTED", "REJECTED" })
	void testFailedInvitationRejectNotPending(final InvitationStatus status) throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setMembers(new HashSet());
		team.setJam(jam);
		User user = new User();
		user.setUsername("spring");
		Invitation invitation = new Invitation();
		invitation.setId(InvitationControllerTests.TEST_INVITATION_ID);
		invitation.setStatus(status);
		invitation.setFrom(team);
		invitation.setTo(user);

		BDDMockito.when(this.invitationService.findInvitationById(InvitationControllerTests.TEST_INVITATION_ID))
				.thenReturn(invitation);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/reject", InvitationControllerTests.TEST_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationRejectNotForYou() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(1));
		team.setMembers(new HashSet());
		team.setJam(jam);
		User user = new User();
		user.setUsername("notSpring");
		Invitation invitation = new Invitation();
		invitation.setId(InvitationControllerTests.TEST_INVITATION_ID);
		invitation.setFrom(team);
		invitation.setTo(user);

		BDDMockito.when(this.invitationService.findInvitationById(InvitationControllerTests.TEST_INVITATION_ID))
				.thenReturn(invitation);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/reject", InvitationControllerTests.TEST_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInvitationRejectJamNotInInscription() throws Exception {
		Team team = new Team();
		team.setId(InvitationControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(InvitationControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(1));
		team.setMembers(new HashSet());
		team.setJam(jam);
		User user = new User();
		user.setUsername("spring");
		Invitation invitation = new Invitation();
		invitation.setId(InvitationControllerTests.TEST_INVITATION_ID);
		invitation.setFrom(team);
		invitation.setTo(user);

		BDDMockito.when(this.invitationService.findInvitationById(InvitationControllerTests.TEST_INVITATION_ID))
				.thenReturn(invitation);

		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/reject", InvitationControllerTests.TEST_INVITATION_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
}