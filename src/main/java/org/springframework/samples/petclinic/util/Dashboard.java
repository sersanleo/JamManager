
package org.springframework.samples.petclinic.util;

import java.util.Collection;
import java.util.Map;
import org.springframework.samples.petclinic.model.Jam;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Dashboard {
	private Long jamsInscription;
	private Long jamsPending;
	private Long jamsInProgress;
	private Long jamsRating;

	private Collection<Jam> activeJams;

	private Map<String, Long> winners;
}
