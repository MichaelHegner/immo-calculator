package ch.hemisoft.immo.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;

import lombok.Data;

@Data
@Embeddable
public class PurchaseCost {
	@Min(0)		double landAcquisition;
	@Min(0)		double notary;
	@Min(0)		double agency;
	@Min(0)		double valuation;
	@Min(0)		double court;
	
	public double getTotalCompletionCost() {
		return landAcquisition + notary + agency + valuation + court;
	}
}
