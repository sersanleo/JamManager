
package org.springframework.samples.petclinic.web;

import java.sql.Date;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
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

	private static final long	DAY_TO_MILLIS	= 1000 * 60 * 60 * 24;

	private static final int	TEST_CREATOR_ID	= 1;

	private static final int	TEST_JAM_ID		= 1;

	@Autowired
	private JamController		jamController;

	@MockBean
	private JamService			jamService;

	@Autowired
	private MockMvc				mockMvc;


	@BeforeEach
	void setup() {
		Jam jam = new Jam();
		jam.setName("Prueba");
		jam.setDescription("Descripción de prueba");
		jam.setDifficulty(1);
		jam.setInscriptionDeadline(new Date(System.currentTimeMillis() + JamControllerTests.DAY_TO_MILLIS * 2));
		jam.setMinTeamSize(2);
		jam.setMaxTeamSize(5);
		jam.setMinTeams(5);
		jam.setMaxTeams(10);
		jam.setStart(new Date(System.currentTimeMillis() + JamControllerTests.DAY_TO_MILLIS * 4));
		jam.setEnd(new Date(System.currentTimeMillis() + JamControllerTests.DAY_TO_MILLIS * 6));

		BDDMockito.given(this.jamService.findJamById(JamControllerTests.TEST_JAM_ID)).willReturn(jam);
		BDDMockito.given(this.jamService.findJams()).willReturn(Lists.newArrayList(jam));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm")).andExpect(MockMvcResultMatchers.model().attributeExists("jam"));
	}

}
