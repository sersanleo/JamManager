package org.springframework.samples.petclinic.web.e2e;



import javax.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class DeliveryControllerE2ETest {

	private static final int TEST_JAM_IN_PROGRESS_ID = 3;
	private static final int TEST_JAM_NOT_IN_PROGRESS_ID = 4;
	private static final int TEST_TEAM_MEMBER_NOT_IN_PROGRESS_ID = 6;
	
	private static final int TEST_TEAM_MEMBER_ID = 4;
	private static final int TEST_NONEXISTENT_TEAM_ID = 100;
	
	private static final int TEST_DELIVERY_ID = 1;
	private static final int TEST_DELIVERY_NOT_IN_PROGRESS_ID = 3;
	
	private static final int TEST_NONEXISTENT_DELIVERY_ID = 100;
	
	@Autowired
	private MockMvc mockMvc;
	
	
	@WithMockUser(username="member1", password="member1", authorities = {"member"})
	@Test
	void testSuccessfulInitDeliveryCreationForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/deliveries/new", 
				DeliveryControllerE2ETest.TEST_JAM_IN_PROGRESS_ID, DeliveryControllerE2ETest.TEST_TEAM_MEMBER_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("deliveries/createForm"))
		.andExpect(MockMvcResultMatchers.model().attributeExists("delivery"));
		
	}
	
	@WithMockUser(username="member2", password="member2", authorities = {"member"})
	@Test
	void testFailedInitDeliveryCreationFormNotMember() throws Exception {
	
		mockMvc.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/deliveries/new", 
				DeliveryControllerE2ETest.TEST_JAM_IN_PROGRESS_ID, DeliveryControllerE2ETest.TEST_TEAM_MEMBER_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	
	@WithMockUser(username="member1", password="member1", authorities = {"member"})
	@Test
	void testFailedInitDeliveryCreationFormNotInProgressJam() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/deliveries/new", 
				DeliveryControllerE2ETest.TEST_JAM_NOT_IN_PROGRESS_ID, DeliveryControllerE2ETest.TEST_TEAM_MEMBER_NOT_IN_PROGRESS_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	
	@WithMockUser(username="member1", password="member1", authorities = {"member"})
	@Test
	void testSuccessfulDeliveryCreation() throws Exception {
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/jams/{jamId}/teams/{teamId}/deliveries/new",
				DeliveryControllerE2ETest.TEST_JAM_IN_PROGRESS_ID, DeliveryControllerE2ETest.TEST_TEAM_MEMBER_ID)
		.with(SecurityMockMvcRequestPostProcessors.csrf())
		.param("description", "test")
		.param("downloadURL", "https://www.google.com/intl/es_ALL/drive/"))
.andExpect(MockMvcResultMatchers.status().is3xxRedirection());	
	}
	
	@WithMockUser(username="member1", password="member1", authorities = {"member"})
	@Test
	void testFailedDeliveryCreationURLNull() throws Exception {
	
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/jams/{jamId}/teams/{teamId}/deliveries/new",
				DeliveryControllerE2ETest.TEST_JAM_IN_PROGRESS_ID, DeliveryControllerE2ETest.TEST_TEAM_MEMBER_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "test")
				.param("downloadURL", ""))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("delivery"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("deliveries/createForm"));
	}
	
	@WithMockUser(username="member1", password="member1", authorities = {"member"})
	@Test
	void testFailedDeliveryCreationInvalidURL() throws Exception {
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/jams/{jamId}/teams/{teamId}/deliveries/new",
				DeliveryControllerE2ETest.TEST_JAM_IN_PROGRESS_ID, DeliveryControllerE2ETest.TEST_TEAM_MEMBER_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "test")
				.param("downloadURL", "no es una url"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("delivery"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("deliveries/createForm"));
	}
	
	
	@WithMockUser(username="member1", password="member1", authorities = {"member"})	
	@Test
	void testFailedDeliveryCreationNotInProgressJam() throws Exception {
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/jams/{jamId}/teams/{teamId}/deliveries/new",
				DeliveryControllerE2ETest.TEST_JAM_NOT_IN_PROGRESS_ID, DeliveryControllerE2ETest.TEST_TEAM_MEMBER_NOT_IN_PROGRESS_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "test")
				.param("downloadURL", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	
	@WithMockUser(username="member2", password="member2", authorities = {"member"})
	@Test
	void testFailedDeliveryCreationNotMember() throws Exception {
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/jams/{jamId}/teams/{teamId}/deliveries/new",
				DeliveryControllerE2ETest.TEST_JAM_IN_PROGRESS_ID, DeliveryControllerE2ETest.TEST_TEAM_MEMBER_ID)
				.with(SecurityMockMvcRequestPostProcessors.csrf())
				.param("description", "test")
				.param("downloadURL", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
		}
	@WithMockUser(username="member1", password="member1", authorities = {"member"})
	@Test
	void testSuccesfulDeliveryDelete() throws Exception {

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/deliveries/{deliveryId}/delete",
								DeliveryControllerE2ETest.TEST_JAM_IN_PROGRESS_ID, DeliveryControllerE2ETest.TEST_TEAM_MEMBER_ID, DeliveryControllerE2ETest.TEST_DELIVERY_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@WithMockUser(username="member2", password="member2", authorities = {"member"})
	@Test
	void testFailedDeliveryDeleteNotMember() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/deliveries/{deliveryId}/delete",
								DeliveryControllerE2ETest.TEST_JAM_IN_PROGRESS_ID, DeliveryControllerE2ETest.TEST_TEAM_MEMBER_ID, DeliveryControllerE2ETest.TEST_DELIVERY_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("exception"));	}
	
	
	@WithMockUser(username="member1", password="member1", authorities = {"member"})
	@Test
	void testFailedDeliveryDeleteNotInProgressJam() throws Exception {
		
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/deliveries/{deliveryId}/delete",
								DeliveryControllerE2ETest.TEST_JAM_NOT_IN_PROGRESS_ID, DeliveryControllerE2ETest.TEST_TEAM_MEMBER_NOT_IN_PROGRESS_ID
								, DeliveryControllerE2ETest.TEST_DELIVERY_NOT_IN_PROGRESS_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("exception"));	}
}

