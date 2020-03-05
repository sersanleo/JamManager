
package org.springframework.samples.petclinic.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

	@NotBlank
	private String				name;

	@NotBlank
	private String				description;

	@Range(min = 1, max = 5)
	@NotNull
	private Integer				difficulty;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm")
	private Date				inscriptionDeadline;

	@NotNull
	@Min(1)
	private Integer				maxTeamSize;

	@NotNull
	@Min(1)
	private Integer				minTeams;

	@NotNull
	@Min(1)
	private Integer				maxTeams;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm")
	private Date				start;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@DateTimeFormat(pattern = "yyyy/MM/dd hh:mm")
	private Date				end;

	// Relationships

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "jam", fetch = FetchType.EAGER)
	private Set<Team>			teams;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "jam", fetch = FetchType.EAGER)
	private Set<JamResource>	resources;

	@OneToOne(optional = true)
	private Team				winner;

	@ManyToOne(optional = false) // Cambiar
	private User				creator;


	@Transient
	public boolean getIsActive() {
		Calendar calendar = new GregorianCalendar();
		return this.inscriptionDeadline.after(calendar.getTime());
	}
}
