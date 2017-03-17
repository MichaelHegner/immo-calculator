package ch.hemisoft.immo.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;

import lombok.Data;

@Data
@Embeddable
public class RunningCost {
	@Min(0)		double administrationEachApartment;
	@Min(0)		double maintenanceEachQm;
}
