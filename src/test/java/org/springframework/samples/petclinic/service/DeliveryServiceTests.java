package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Delivery;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class DeliveryServiceTests {

	@Autowired
	protected DeliveryService deliveryService;

	@Autowired
	protected TeamService teamService;

	@Test
	void shouldFindDeliveryById() {
		Delivery delivery = this.deliveryService.findDeliveryById(1);
		Assertions.assertThat(delivery).isNotEqualTo(null);
	}

	@Test
	void shouldNotFindDeliveryByInexistentId() {
		Assertions.assertThatThrownBy(() -> this.deliveryService.findDeliveryById(200))
				.isInstanceOf(NoSuchElementException.class);
	}
	
	@Test
	void shouldFindDeliveryByTeam() {
		Assertions.assertThat(this.deliveryService.findDeliverysByTeam(this.teamService.findTeamById(4)).size())
		.isEqualTo(2);
	}	
	
	@Test
	void shouldNotFindDeliveryByTeam() {
		Assertions.assertThat(this.deliveryService.findDeliverysByTeam(this.teamService.findTeamById(1)).size())
		.isEqualTo(0);
	}	

	@Test
	@Transactional
	public void shouldInsertDeliveryAndGenerateId() {
		Collection<Delivery> deliveries = this.deliveryService.findDeliverysByTeam(this.teamService.findTeamById(4));
		int found = deliveries.size();

		Delivery delivery = new Delivery();
		delivery.setDescription("Una buena descripcion");
		delivery.setDownloadURL("https://www.youtube.com/");
		delivery.setTeam(this.teamService.findTeamById(4));

		this.deliveryService.saveDelivery(delivery);
		Assertions.assertThat(delivery.getId()).isNotNull();

		deliveries = this.deliveryService.findDeliverysByTeam(this.teamService.findTeamById(4));
		Assertions.assertThat(deliveries.size()).isEqualTo(found + 1);
	}
	
	@Test
	public void shouldNotInsertNullDelivery() {
		Assertions.assertThatThrownBy(() -> this.deliveryService.saveDelivery(null)).isInstanceOf(Exception.class);
	}
	
	@Test
	@Transactional
	public void shouldDeleteDelivery() {
		Delivery delivery = this.deliveryService.findDeliveryById(1);
		Assertions.assertThat(delivery).isNotNull();

		this.deliveryService.deleteDelivery(delivery);
		Assertions.assertThatThrownBy(() -> this.deliveryService.findDeliveryById(1))
				.isInstanceOf(NoSuchElementException.class);
	}

	@Test
	public void shouldNotDeleteNullDelivery() {
		Assertions.assertThatThrownBy(() -> this.deliveryService.deleteDelivery(null)).isInstanceOf(Exception.class);
	}
}
