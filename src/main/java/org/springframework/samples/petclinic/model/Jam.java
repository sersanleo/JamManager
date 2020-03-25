
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "jams")
public class Jam extends BaseEntity {

	@Override
	public String toString() {
		return "Jam [name=" + this.name + ", description=" + this.description + ", difficulty=" + this.difficulty + ", inscriptionDeadline=" + this.inscriptionDeadline + ", maxTeamSize=" + this.maxTeamSize + ", minTeams=" + this.minTeams + ", maxTeams="
			+ this.maxTeams + ", start=" + this.start + ", end=" + this.end + ", teams=" + this.teams + ", resources=" + this.resources + ", winner=" + this.winner + ", creator=" + this.creator + "]";
	}


	@NotBlank
	private String				name;

	@NotBlank
	private String				description;

	@NotNull
	@Range(min = 1, max = 5)
	private Integer				difficulty;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-M-d HH:mm")
	private LocalDateTime		inscriptionDeadline;

	@NotNull
	@Min(1)
	private Integer				maxTeamSize;

	@NotNull
	@Min(1)
	private Integer				minTeams;

	@NotNull
	@Min(1)
	private Integer				maxTeams;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-M-d HH:mm")
	private LocalDateTime		start;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-M-d HH:mm")
	private LocalDateTime		end;

	@NotNull
	private Boolean				rated;

	// Relationships

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "jam", fetch = FetchType.EAGER)
	private Set<Team>			teams;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "jam", fetch = FetchType.EAGER)
	private Set<JamResource>	resources;

	@OneToOne(optional = true)
	private Team				winner;

	@ManyToOne(optional = false)
	private User				creator;


	public Jam() {
		super();
		this.rated = false;
	}

	@Transient
	public JamStatus getStatus() {
		if (!this.rated) {
			LocalDateTime now = LocalDateTime.now();
			if (now.isBefore(this.inscriptionDeadline)) {
				return JamStatus.INSCRIPTION;
			} else if (this.teams.size() < this.minTeams) {
				return JamStatus.CANCELLED;
			} else if (now.isBefore(this.start)) {
				return JamStatus.PENDING;
			} else if (now.isBefore(this.end)) {
				return JamStatus.IN_PROGRESS;
			} else if (!this.rated) {
				return JamStatus.RATING;
			}
		}
		return JamStatus.FINISHED;
	}
}
