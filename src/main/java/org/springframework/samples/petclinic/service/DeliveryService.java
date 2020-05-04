package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Delivery;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.repository.DeliveryRepository;
import org.springframework.samples.petclinic.repository.InvitationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeliveryService {
	
	@Autowired
	private DeliveryRepository deliveryRepository;

	@Transactional(readOnly = true)
	public Delivery findDeliveryById(final int id) throws DataAccessException {
		return this.deliveryRepository.findById(id).get();
	}

	@Transactional
	public Collection<Delivery> findDeliverysByTeam(final Team team) {
		return this.deliveryRepository.findDeliverysByTeam(team);
	}

	@Transactional
	public void saveDelivery(final Delivery delivery) {
		this.deliveryRepository.save(delivery);
	}

	@Transactional
	public void deleteDelivery(final Delivery delivery) {
		this.deliveryRepository.delete(delivery);
	}



}
