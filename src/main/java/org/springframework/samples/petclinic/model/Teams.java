package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Teams {

	private List<Team> teams;


	@XmlElement
	public List<Team> getTeamList() {
		if (this.teams == null) {
			this.teams = new ArrayList<>();
		}
		return this.teams;
	}
	
}
