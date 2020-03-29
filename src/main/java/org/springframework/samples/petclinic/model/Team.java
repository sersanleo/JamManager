
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "teams")
public class Team extends BaseEntity {

	@NotBlank
	private String name;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-M-d HH:mm")
	@Past
	private LocalDateTime creationDate;

	// Relationships

	@ManyToOne(optional = false)
	private Jam jam;

	@ManyToMany()
	private Set<User> members;

	@ManyToMany(cascade = CascadeType.REMOVE, mappedBy = "from", fetch = FetchType.EAGER)
	private Set<Invitation> invitations;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "team", fetch = FetchType.EAGER)
	private Set<Mark> marks;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "team", fetch = FetchType.EAGER)
	private Set<Delivery> deliveries;

	public Team() {
		super();
		this.creationDate = LocalDateTime.now().minusNanos(1);
	}

	protected Set<Invitation> getInvitationInternal(){
		if (this.invitations == null) {
			this.invitations = new HashSet<>();
		}
		return this.invitations;
	}
	
	protected void setInviationInternal(Set<Invitation> invitation) {
		this.invitations = invitation;
	}
	
	public List<Invitation> getInvitations(){
		List<Invitation> sortedInvitation = new ArrayList<>(getInvitationInternal());
		return Collections.unmodifiableList(sortedInvitation);
	}
	
	public void addInvitation(Invitation invitation) {
		getInvitationInternal().add(invitation);
		invitation.setFrom(this);
	}
	
	
	public void deleteInvitation(Invitation invitation) {
		getInvitationInternal().remove(invitation);
	}
}
