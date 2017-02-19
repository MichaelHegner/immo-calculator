package ch.hemisoft.immo.domain;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class PurchaseCost {
	double landAcquisition;
	double notary;
	double agency;
	double valuation;
	
	public double getTotalCompletionCost() {
		return landAcquisition + notary + agency + valuation;
	}
}