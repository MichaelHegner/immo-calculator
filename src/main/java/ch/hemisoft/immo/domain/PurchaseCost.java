package ch.hemisoft.immo.domain;

import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;

import lombok.Data;

@Data
@Embeddable
public class PurchaseCost {
	@Min(0)		BigDecimal landAcquisition;
	@Min(0)		BigDecimal notary;
	@Min(0)		BigDecimal agency;
	@Min(0)		BigDecimal valuation;
	@Min(0)		BigDecimal court;
	
	public double getTotalCompletionCost() {
		return landAcquisition.doubleValue() + notary.doubleValue() + agency.doubleValue() + valuation.doubleValue() + court.doubleValue();
	}
}
