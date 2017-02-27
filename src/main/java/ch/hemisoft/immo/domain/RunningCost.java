package ch.hemisoft.immo.domain;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class RunningCost {
	double administrationEachApartment;
	double maintenanceEachQm;
}
