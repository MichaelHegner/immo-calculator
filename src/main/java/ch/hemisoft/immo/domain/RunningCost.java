package ch.hemisoft.immo.domain;

import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;

import lombok.Data;

@Data
@Embeddable
public class RunningCost {
	@Min(0)		BigDecimal administrationEachApartment;
	@Min(0)		BigDecimal maintenanceEachQm;
}
