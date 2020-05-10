package org.springframework.samples.petclinic.repository;



import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Dashboard;
import org.springframework.samples.petclinic.model.Jam;


public interface DashboardRepository extends  CrudRepository<Dashboard, Integer> {
	
	
	@Query("SELECT count(a) FROM Jam a")
	Integer countJams();
	
	@Query("SELECT count(t) FROM Team t")
	Integer countTeams();
	
	@Query("SELECT j FROM Jam j WHERE YEAR(j.start)=:year AND MONTH(j.start)=:month")
	Collection<Jam> findPastJamByMonthAndYear(Integer month, Integer year);
	

	
//	@Query("select 1.0 * count(a) / (select count(b) from Jam b) from Jam a where a.JamStatus = org.springframework.samples.petclinic.model.Jam.JamStatus.INSCRIPTION")
//	Double ratioInscriptionJams();
	

}
