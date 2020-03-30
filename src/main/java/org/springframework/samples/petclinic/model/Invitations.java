package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Invitations {

	private List<Invitation> invitations;


	@XmlElement
	public List<Invitation> getInvitationList() {
		if (this.invitations == null) {
			this.invitations = new ArrayList<>();
		}
		return this.invitations;
	}

}
