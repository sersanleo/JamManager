package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.datatypes.Phone;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.JamResource;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.InvitationService;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = InvitationController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class InvitationControllerTests {

	private static final int TEST_JAM_ID = 1;
	private static final int TEST_TEAM_ID = 1;
	private static final int TEST_INVITATION_ID = 1;

	@MockBean
	private InvitationService invitationService;

	@MockBean
	private UserService userService;

	@MockBean
	private TeamService teamService;

	@MockBean
	private JamService jamService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	private void beforeEach() {
		User memberUser = new User();
		memberUser.setUsername("memberUser");
		memberUser.setPassword("memberUser");
		memberUser.setEnabled(true);
		memberUser.setEmail("example@example.com");
		memberUser.setPhone(new Phone());

		User nonMemberUser = new User();
		nonMemberUser.setUsername("nonMemberUser");
		nonMemberUser.setPassword("nonMemberUser");
		nonMemberUser.setEnabled(true);
		nonMemberUser.setEmail("example@example.com");
		nonMemberUser.setPhone(new Phone());

		User pendingUser = new User();
		nonMemberUser.setUsername("pendingUser");
		nonMemberUser.setPassword("pendingUser");
		nonMemberUser.setEnabled(true);
		nonMemberUser.setEmail("example@example.com");
		nonMemberUser.setPhone(new Phone());

		Jam jam = new Jam();
		jam.setName("Inscription Jam");
		jam.setDescription("This is a test Jam.");
		jam.setDifficulty(5);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(2));
		jam.setMaxTeamSize(5);
		jam.setMinTeams(5);
		jam.setMaxTeams(12);
		jam.setStart(LocalDateTime.now().plusDays(3));
		jam.setEnd(LocalDateTime.now().plusDays(4));
		jam.setJamResources(new HashSet<JamResource>());

		Team team = new Team();
		team.setJam(jam);
		team.setName("team1");
		Set<User> members = new HashSet<>();
		members.add(memberUser);
		team.setMembers(members);
		Set<Team> teams = new HashSet<>();
		teams.add(team);
		jam.setTeams(teams);

		Invitation invitation = new Invitation();
		invitation.setFrom(team);
		invitation.setTo(pendingUser);

		BDDMockito.given(this.invitationService.findInvitationById(InvitationControllerTests.TEST_INVITATION_ID))
				.willReturn(invitation);
		BDDMockito.given(this.jamService.findJamById(InvitationControllerTests.TEST_JAM_ID)).willReturn(jam);
		BDDMockito.given(this.teamService.findTeamById(InvitationControllerTests.TEST_TEAM_ID)).willReturn(team);

		BDDMockito.given(this.userService.findByUsername("memberUser")).willReturn(memberUser);
		BDDMockito.given(this.userService.findByUsername("nonMemberUser")).willReturn(nonMemberUser);
		BDDMockito.given(this.userService.findByUsername("pendingUser")).willReturn(pendingUser);

		BDDMockito.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"spring")).willReturn(true);
		BDDMockito.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(InvitationControllerTests.TEST_TEAM_ID,
				"memberUser")).willReturn(true);
		BDDMockito.given(this.invitationService.findHasPendingInvitationsByTeamIdAndUsername(
				InvitationControllerTests.TEST_TEAM_ID,
				"pendingUser")).willReturn(true);
	}

	@WithMockUser(value = "spring")
	@Test
	void testListUserInvitations() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/invitations"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("invitations/invitationList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("invitations"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitInvitationCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams/1/teams/1/invitations/new"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("invitation"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulInvitationCreation() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/jams/1/teams/1/invitations/new")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "nonMemberUser"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedUsernameInexistentInvitationCreation() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/jams/1/teams/1/invitations/new")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "nonExistentUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("invitation"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("invitation", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("invitation", "to.username"))
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"));
		System.out.println(MockMvcResultMatchers.view());
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedUsernameInTeamInvitationCreation() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/jams/1/teams/1/invitations/new")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "memberUser"))
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
	void testFailedPendingInvitationInvitationCreation() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/jams/1/teams/1/invitations/new")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "pendingUser"))
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
	void testSuccesfulInvitationRemove() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete", 1, 1, 1)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulInvitationAccept() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/accept", 1)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulInvitationReject() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/invitations/{invitationId}/reject", 1)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
	}
}