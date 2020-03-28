
package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for the {@link PetController}
 *
 * @author Colin But
 */
@WebMvcTest(value = JamController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class JamControllerTests {

	private static final int TEST_CREATOR_ID = 1;
	private static final int TEST_JAM_ID = 1;

	@Autowired
	private JamController jamController;

	@MockBean
	private JamService jamService;

	@Autowired
	private MockMvc mockMvc;

	private Jam createDummyJam() {
		Jam jam = new Jam();
		jam.setName("Test Jam");
		jam.setDescription("This is a test Jam.");
		jam.setDifficulty(5);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMaxTeamSize(5);
		jam.setMinTeams(5);
		jam.setMaxTeams(12);
		jam.setStart(LocalDateTime.now().plusDays(7));
		jam.setEnd(LocalDateTime.now().plusDays(9));

		return jam;
	}

	@WithMockUser(username = "anonymous", roles = {})
	@Test
	void shouldShowJams() throws Exception {
		Jam dummyJam = this.createDummyJam();

		BDDMockito.given(this.jamService.findJamById(JamControllerTests.TEST_JAM_ID)).willReturn(dummyJam);
		BDDMockito.given(this.jamService.findJams()).willReturn(Lists.newArrayList(dummyJam));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/jamList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jams"));
	}

	@WithMockUser(username = "jamOrganizator", roles = { "jamOrganizator" })
	@Test
	void shouldShowCreateJamForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jam"));
	}

	@WithMockUser(username = "jamOrganizator", roles = { "jamOrganizator" })
	@Test
	void shouldCreateJam() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", "2025-6-1 12:00")
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", "2025-6-5 12:00")
						.param("end", "2025-6-7 12:00"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "jamOrganizator", roles = { "jamOrganizator" })
	@Test
	void shouldNotCreateJam() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", "2019-6-1 12:00")
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", "2025-6-5 12:00")
						.param("end", "2025-6-7 12:00"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "inscriptionDeadline"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}
}
