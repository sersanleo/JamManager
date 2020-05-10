
package org.springframework.samples.petclinic.service;


import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.repository.DashboardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class DashboardService {

	@Autowired
	private DashboardRepository repository;
	
	@Transactional
	public Integer countJams() {
		return this.repository.countJams();
	}
	
	@Transactional
	public Integer countTeams() {
		return this.repository.countTeams();
	}
	
	public Collection<Jam> findJamsOfMonth(Integer month) {

		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		Integer year = cal.get(Calendar.YEAR);
		
		return this.repository.findPastJamByMonthAndYear(month,year);
	}

}
