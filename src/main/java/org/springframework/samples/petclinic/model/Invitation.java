
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Invitation extends BaseEntity {

	@NotNull
	@DateTimeFormat(pattern = "yyyy-M-d HH:mm")
	@Past
	private LocalDateTime		creationDate;

	@NotNull
	private InvitationStatus	status;

	// Relationships

	@ManyToOne(optional = false)
	private Team				from;

	@ManyToOne(optional = false)
	private User				to;
}
