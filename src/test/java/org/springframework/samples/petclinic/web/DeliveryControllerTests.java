package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Delivery;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.DeliveryService;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = DeliveryController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class DeliveryControllerTests {

	private static final int TEST_JAM_ID = 1;
	private static final int TEST_TEAM_ID = 1;
	private static final int TEST_NONEXISTENT_TEAM_ID = 100;
	
	private static final int TEST_DELIVERY_ID = 1;
	private static final int TEST_NONEXISTENT_DELIVERY_ID = 100;
	
	
	@MockBean
	private TeamService teamService;
	@MockBean
	private UserService userService;
	@MockBean
	private JamService jamService;
	@MockBean
	private AuthoritiesService authoritiesService;
	@MockBean
	private DeliveryService deliveryService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	private void beforeEach() {
		BDDMockito.when(this.deliveryService.findDeliveryById(DeliveryControllerTests.TEST_NONEXISTENT_DELIVERY_ID))
				.thenThrow(NoSuchElementException.class);
		BDDMockito
				.when(this.teamService
						.findTeamById(DeliveryControllerTests.TEST_NONEXISTENT_TEAM_ID))
				.thenThrow(NoSuchElementException.class);
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testSuccessfulInitDeliveryCreationForm() throws Exception {
		Team team = new Team();
		team.setId(DeliveryControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(DeliveryControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(5));
		jam.setStart(LocalDateTime.now().minusDays(3));
		jam.setEnd(LocalDateTime.now().plusDays(1));
		jam.setMinTeams(1);
		jam.setTeams(new HashSet<Team>() {
			{
				this.add(team);
			}
		});
		jam.setMaxTeamSize(5);
		team.setJam(jam);

		BDDMockito.when(this.teamService.findTeamById(DeliveryControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(DeliveryControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/deliveries/new",
						DeliveryControllerTests.TEST_JAM_ID, DeliveryControllerTests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("deliveries/createForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("delivery"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testFailedInitDeliveryCreationFormNotMember() throws Exception {
		Team team = new Team();
		team.setId(DeliveryControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(DeliveryControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(5));
		jam.setStart(LocalDateTime.now().minusDays(3));
		jam.setEnd(LocalDateTime.now().plusDays(1));
		jam.setMinTeams(1);
		jam.setTeams(new HashSet<Team>() {
			{
				this.add(team);
			}
		});
		jam.setMaxTeamSize(5);
		team.setJam(jam);

		BDDMockito.when(this.teamService.findTeamById(DeliveryControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(DeliveryControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(false);
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/deliveries/new",
						DeliveryControllerTests.TEST_JAM_ID, DeliveryControllerTests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testFailedInitDeliveryCreationFormNotInProgressJam() throws Exception {
		Team team = new Team();
		team.setId(DeliveryControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(DeliveryControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMinTeams(1);
		jam.setTeams(new HashSet<Team>() {
			{
				this.add(team);
			}
		});
		jam.setMaxTeamSize(5);
		team.setJam(jam);

		BDDMockito.when(this.teamService.findTeamById(DeliveryControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(DeliveryControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/deliveries/new",
						DeliveryControllerTests.TEST_JAM_ID, DeliveryControllerTests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testSuccessfulDeliveryCreation() throws Exception {
		Team team = new Team();
		team.setId(DeliveryControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(DeliveryControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(5));
		jam.setStart(LocalDateTime.now().minusDays(3));
		jam.setEnd(LocalDateTime.now().plusDays(1));
		jam.setMinTeams(1);
		jam.setTeams(new HashSet<Team>() {
			{
				this.add(team);
			}
		});
		jam.setMaxTeamSize(5);
		team.setJam(jam);

		BDDMockito.when(this.teamService.findTeamById(DeliveryControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(DeliveryControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/jams/{jamId}/teams/{teamId}/deliveries/new",
						DeliveryControllerTests.TEST_JAM_ID, DeliveryControllerTests.TEST_TEAM_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "test")
				.param("downloadURL", "https://www.google.com/intl/es_ALL/drive/"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testFailedDeliveryCreationURLNull() throws Exception {
		Team team = new Team();
		team.setId(DeliveryControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(DeliveryControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(5));
		jam.setStart(LocalDateTime.now().minusDays(3));
		jam.setEnd(LocalDateTime.now().plusDays(1));
		jam.setMinTeams(1);
		jam.setTeams(new HashSet<Team>() {
			{
				this.add(team);
			}
		});
		jam.setMaxTeamSize(5);
		team.setJam(jam);

		BDDMockito.when(this.teamService.findTeamById(DeliveryControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(DeliveryControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/jams/{jamId}/teams/{teamId}/deliveries/new",
						DeliveryControllerTests.TEST_JAM_ID, DeliveryControllerTests.TEST_TEAM_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "test")
				.param("downloadURL", ""))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("delivery"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("deliveries/createForm"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testFailedDeliveryCreationInvalidURL() throws Exception {
		Team team = new Team();
		team.setId(DeliveryControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(DeliveryControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(5));
		jam.setStart(LocalDateTime.now().minusDays(3));
		jam.setEnd(LocalDateTime.now().plusDays(1));
		jam.setMinTeams(1);
		jam.setTeams(new HashSet<Team>() {
			{
				this.add(team);
			}
		});
		jam.setMaxTeamSize(5);
		team.setJam(jam);

		BDDMockito.when(this.teamService.findTeamById(DeliveryControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(DeliveryControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/jams/{jamId}/teams/{teamId}/deliveries/new",
						DeliveryControllerTests.TEST_JAM_ID, DeliveryControllerTests.TEST_TEAM_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "test")
				.param("downloadURL", "no es una url"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("delivery"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("deliveries/createForm"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testFailedDeliveryCreationNotInProgressJam() throws Exception {
		Team team = new Team();
		team.setId(DeliveryControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(DeliveryControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMinTeams(1);
		jam.setTeams(new HashSet<Team>() {
			{
				this.add(team);
			}
		});
		jam.setMaxTeamSize(5);
		team.setJam(jam);

		BDDMockito.when(this.teamService.findTeamById(DeliveryControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(DeliveryControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/jams/{jamId}/teams/{teamId}/deliveries/new",
						DeliveryControllerTests.TEST_JAM_ID, DeliveryControllerTests.TEST_TEAM_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "test")
				.param("downloadURL", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testFailedDeliveryCreationNotMember() throws Exception {
		Team team = new Team();
		team.setId(DeliveryControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(DeliveryControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(5));
		jam.setStart(LocalDateTime.now().minusDays(3));
		jam.setEnd(LocalDateTime.now().plusDays(1));
		jam.setMinTeams(1);
		jam.setTeams(new HashSet<Team>() {
			{
				this.add(team);
			}
		});
		jam.setMaxTeamSize(5);
		team.setJam(jam);

		BDDMockito.when(this.teamService.findTeamById(DeliveryControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(DeliveryControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(false);
		
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/jams/{jamId}/teams/{teamId}/deliveries/new",
						DeliveryControllerTests.TEST_JAM_ID, DeliveryControllerTests.TEST_TEAM_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "test")
				.param("downloadURL", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
		}
	
	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulDeliveryDelete() throws Exception {
		Team team = new Team();
		team.setId(DeliveryControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(DeliveryControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(5));
		jam.setStart(LocalDateTime.now().minusDays(3));
		jam.setEnd(LocalDateTime.now().plusDays(1));
		jam.setMinTeams(1);
		jam.setTeams(new HashSet<Team>() {
			{
				this.add(team);
			}
		});
		jam.setMaxTeamSize(5);
		team.setJam(jam);
		Delivery delivery = new Delivery();
		delivery.setDescription("test");
		delivery.setDownloadURL("https://www.google.com/intl/es_ALL/drive/");
		delivery.setTeam(team);

		BDDMockito.when(this.teamService.findTeamById(DeliveryControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(DeliveryControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);
		BDDMockito.when(this.deliveryService.findDeliveryById(TEST_DELIVERY_ID)).thenReturn(delivery);
		
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/deliveries/{deliveryId}/delete",
								DeliveryControllerTests.TEST_JAM_ID, DeliveryControllerTests.TEST_TEAM_ID, DeliveryControllerTests.TEST_DELIVERY_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testFailedDeliveryDeleteNotMember() throws Exception {
		Team team = new Team();
		team.setId(DeliveryControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(DeliveryControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(5));
		jam.setStart(LocalDateTime.now().minusDays(3));
		jam.setEnd(LocalDateTime.now().plusDays(1));
		jam.setMinTeams(1);
		jam.setTeams(new HashSet<Team>() {
			{
				this.add(team);
			}
		});
		jam.setMaxTeamSize(5);
		team.setJam(jam);
		Delivery delivery = new Delivery();
		delivery.setDescription("test");
		delivery.setDownloadURL("https://www.google.com/intl/es_ALL/drive/");
		delivery.setTeam(team);

		BDDMockito.when(this.teamService.findTeamById(DeliveryControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(DeliveryControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(false);
		BDDMockito.when(this.deliveryService.findDeliveryById(TEST_DELIVERY_ID)).thenReturn(delivery);
		
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/deliveries/{deliveryId}/delete",
								DeliveryControllerTests.TEST_JAM_ID, DeliveryControllerTests.TEST_TEAM_ID, DeliveryControllerTests.TEST_DELIVERY_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("exception"));	}
	@WithMockUser(value = "spring")
	@Test
	void testFailedDeliveryDeleteNotInProgressJam() throws Exception {
		Team team = new Team();
		team.setId(DeliveryControllerTests.TEST_TEAM_ID);
		Jam jam = new Jam();
		jam.setId(DeliveryControllerTests.TEST_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMinTeams(1);
		jam.setTeams(new HashSet<Team>() {
			{
				this.add(team);
			}
		});
		jam.setMaxTeamSize(5);
		team.setJam(jam);
		Delivery delivery = new Delivery();
		delivery.setDescription("test");
		delivery.setDownloadURL("https://www.google.com/intl/es_ALL/drive/");
		delivery.setTeam(team);

		BDDMockito.when(this.teamService.findTeamById(DeliveryControllerTests.TEST_TEAM_ID)).thenReturn(team);
		BDDMockito.when(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(DeliveryControllerTests.TEST_TEAM_ID,
				"spring")).thenReturn(true);
		BDDMockito.when(this.deliveryService.findDeliveryById(TEST_DELIVERY_ID)).thenReturn(delivery);
		
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/deliveries/{deliveryId}/delete",
								DeliveryControllerTests.TEST_JAM_ID, DeliveryControllerTests.TEST_TEAM_ID, DeliveryControllerTests.TEST_DELIVERY_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("exception"));	}
}
