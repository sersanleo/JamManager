package org.springframework.samples.petclinic.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Authorities;

public interface AuthoritiesRepository extends  CrudRepository<Authorities, String>{
	
	@Query("SELECT COUNT(a)>0 FROM Authorities a WHERE a.username=?1 AND a.authority=?2")
	boolean findHasAuthorityByUsername(String username, String authority);
	
}
