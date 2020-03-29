package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.datatypes.Phone;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.model.InvitationStatus;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = InvitationController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class InvitationControllerTests {

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
		User to = new User();
		to.setUsername("to");
		to.setPassword("to");
		to.setEnabled(true);
		to.setEmail("to@to.com");
		to.setPhone(new Phone());

		User user1 = new User();
		user1.setUsername("sample1");
		user1.setPassword("sample1");
		user1.setEnabled(true);
		user1.setEmail("sample1@sample.com");
		user1.setPhone(new Phone());

		User creator = new User();
		creator.setUsername("creator");
		creator.setPassword("creator");
		creator.setEnabled(true);
		creator.setEmail("creator@creator.com");
		creator.setPhone(new Phone());

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

		Team from = new Team();
		from.setJam(jam);
		from.setName("team1");
		Set<User> members = new HashSet<>();
		members.add(user1);
		from.setMembers(members);
		Set<Team> teams = new HashSet<>();
		teams.add(from);
		jam.setTeams(teams);

		Invitation invitation = new Invitation();
		invitation.setFrom(from);
		invitation.setTo(to);
		invitation.setCreationDate(LocalDateTime.now().minusNanos(1));
		invitation.setStatus(InvitationStatus.PENDING);

		BDDMockito.given(this.invitationService.findInvitationById(InvitationControllerTests.TEST_INVITATION_ID))
				.willReturn(invitation);
		BDDMockito.given(this.jamService.findJamById(1)).willReturn(jam);
		BDDMockito.given(this.teamService.findTeamById(1)).willReturn(from);
	}

	@WithMockUser(value = "spring")
	@Test
	void testListUserInvitations() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/invitations"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("users/invitationListUser"))
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
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "creator"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedUsernameInexistentInvitationCreation() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/jams/1/teams/1/invitations/new")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("invitation"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("invitation", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("invitation", "to"))
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedUsernameInTeamInvitationCreation() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/jams/1/teams/1/invitations/new")
						.with(SecurityMockMvcRequestPostProcessors.csrf()).param("to.username", "user1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("invitation"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("invitation", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("invitation", "to"))
				.andExpect(MockMvcResultMatchers.view().name("invitations/createForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulInvitationRemove() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete")
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
}