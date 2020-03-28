
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "invitations")
public class Invitation extends BaseEntity {

	@Override
	public String toString() {
		return "Invitation [creationDate=" + this.creationDate + ", status=" + this.status + ", from=" + this.from
				+ ", to=" + this.to + ", id=" + this.id + "]";
	}

	@NotNull
	@DateTimeFormat(pattern = "yyyy-M-d HH:mm")
	@Past
	private LocalDateTime creationDate;

	@NotNull
	private InvitationStatus status;

	// Relationships

	@ManyToOne(optional = false)
	@JoinColumn(name = "team_id")
	private Team from;

	@ManyToOne(optional = false)
	@JoinColumn(name = "to_username")
	private User to;

	public Invitation() {
		super();
		this.creationDate = LocalDateTime.now().minusNanos(1);
		this.status = InvitationStatus.PENDING;
	}
}
