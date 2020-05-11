package org.springframework.samples.petclinic.web.integration;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Delivery;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.JamStatus;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.DeliveryService;
import org.springframework.samples.petclinic.service.InvitationService;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.web.DeliveryController;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.MapBindingResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

class DeliveryControllerIntegrationTests {

	private static final int TEST_JAM_IN_PROGRESS_ID = 3;
	private static final int TEST_TEAM_MEMBER_ID = 4;
	private static final int TEST_NONEXISTENT_TEAM_ID = 100;
	
	private static final int TEST_DELIVERY_ID = 1;
	private static final int TEST_NONEXISTENT_DELIVERY_ID = 100;
	
	@Autowired
	private DeliveryController deliveryController;
	
	@Autowired
	private TeamService teamService;
	@Autowired
	private JamService jamService;


	@WithMockUser(username="member1", password="member1", authorities = {"member"})
	@Test
	void testSuccessfulInitDeliveryCreationForm() throws Exception {
		Team team = teamService.findTeamById(TEST_TEAM_MEMBER_ID);
		ModelMap model = new ModelMap();
		model.put("delivery", new Delivery());
		String view = deliveryController.initCreationForm(TEST_TEAM_MEMBER_ID, model);
		
		assertEquals(view, "deliveries/createForm");
		assertNotNull(model.get("delivery"));
	}
	
	@WithMockUser(username="member1", password="member1", authorities = {"member"})
	@Test
	void testSuccessfulDeliveryCreation() throws Exception {
		Delivery delivery = new Delivery();
		delivery.setDescription("Una buena descripcion");
		delivery.setDownloadURL("https://www.youtube.com/");
		delivery.setTeam(this.teamService.findTeamById(TEST_TEAM_MEMBER_ID));
		BindingResult bindingResult = new MapBindingResult(Collections.emptyMap(), "");
		
		String view = deliveryController.processCreationForm(delivery, bindingResult, TEST_TEAM_MEMBER_ID);
		assertEquals(view, "redirect:/jams/{jamId}/teams/{teamId}");
	}
	
	
	
}
