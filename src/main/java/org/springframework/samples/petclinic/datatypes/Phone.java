
package org.springframework.samples.petclinic.datatypes;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Phone implements Serializable {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Range(min = 1, max = 999)
	private Integer countryCode;

	@Pattern(regexp = "\\d{1,6}", message = "default.error.conversion")
	private String areaCode;

	@Pattern(regexp = "\\d{1,9}([\\s-]\\d{1,9}){0,4}", message = "default.error.conversion")
	private String number;

	public Phone() {
		super();
	}

	public Phone(@NotNull @Range(min = 1, max = 999) final Integer countryCode,
			@Pattern(regexp = "\\d{1,6}", message = "default.error.conversion") final String areaCode,
			@Pattern(regexp = "\\d{1,9}([\\s-]\\d{1,9}){0,4}", message = "default.error.conversion") final String number) {
		super();
		this.countryCode = countryCode;
		this.areaCode = areaCode;
		this.number = number;
	}

	@Override
	public String toString() {
		StringBuilder result;

		result = new StringBuilder();
		result.append("+");
		result.append(this.countryCode);

		if (this.areaCode == null) {
			result.append(" ");
		} else {
			result.append(" (");
			result.append(this.areaCode);
			result.append(") ");
		}

		result.append(this.number);

		return result.toString();
	}

}
