
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
import javax.persistence.Transient;
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
		this.members = new HashSet();
		this.invitations = new HashSet();
	}

	protected Set<Invitation> getInvitationInternal() {
		if (this.invitations == null) {
			this.invitations = new HashSet<>();
		}
		return this.invitations;
	}

	protected void setInviationInternal(final Set<Invitation> invitation) {
		this.invitations = invitation;
	}

	public List<Invitation> getInvitations() {
		List<Invitation> sortedInvitation = new ArrayList<>(this.getInvitationInternal());
		return Collections.unmodifiableList(sortedInvitation);
	}

	public void addInvitation(final Invitation invitation) {
		this.getInvitationInternal().add(invitation);
		invitation.setFrom(this);
	}

	public void deleteInvitation(final Invitation invitation) {
		this.getInvitationInternal().remove(invitation);
	}

	@Transient
	public Float getAverage() {
		Float average = null;
		if (!this.marks.isEmpty()) {
			average = 0f;
			for (Mark mark : this.marks) {
				average += mark.getValue();
			}
			average /= (float) this.marks.size();
		}
		return average;
	}
	
	@Transient
	public boolean getIsFull() {
		return invitations.stream().filter(x->x.getStatus() == InvitationStatus.PENDING).count() + members.size() >= jam.getMaxTeamSize();
	}

	public Boolean isMarkedBy(final String username) {
		for (Mark mark : this.marks) {
			if (mark.getJudge().getUsername().equals(username)) {
				return true;
			}
		}

		return false;
	}
	
	
	
	protected Set<Delivery> getDeliveryInternal() {
		if (this.deliveries == null) {
			this.deliveries = new HashSet<>();
		}
		return this.deliveries;
	}

	protected void setDeliveryInternal(final Set<Delivery> delivery) {
		this.deliveries = delivery;
	}

	public List<Delivery> getDeliveries() {
		List<Delivery> sortedDelivery = new ArrayList<>(this.getDeliveryInternal());
		return Collections.unmodifiableList(sortedDelivery);
	}

	public void addDelivery(final Delivery delivery) {
		this.getDeliveryInternal().add(delivery);
		delivery.setTeam(this);
	}

	public void deleteDelivery(final Delivery delivery) {
		this.getDeliveryInternal().remove(delivery);
	}


}
