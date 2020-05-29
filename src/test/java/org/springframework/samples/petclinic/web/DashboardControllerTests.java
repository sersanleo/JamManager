package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.samples.petclinic.model.Jam;

@WebMvcTest(controllers = DashboardController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class DashboardControllerTests {
	@MockBean
	private JamService jamService;

	@Autowired
	private MockMvc mockMvc;

	private static final LocalDateTime futureDateTime(final int daysOffset) {
		return LocalDateTime.now().plusDays(daysOffset);
	}

	@BeforeEach
	private void beforeEach() {
		Collection<Jam> jams = new HashSet();

		Jam testJam = new Jam();
		testJam.setName("Test Jam");
		testJam.setInscriptionDeadline(futureDateTime(3));
		testJam.setStart(futureDateTime(5));
		testJam.setEnd(futureDateTime(7));

		jams.add(testJam);

		BDDMockito.when(this.jamService.findJams()).thenReturn(jams);
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowDashboard() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/dashboard"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("/dashboard/dashboard"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("dashboard"));
	}
}
