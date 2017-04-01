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
		double dLandAcquisition 	= null == landAcquisition 	? 0.0 : landAcquisition.doubleValue();
		double dNotary 				= null == notary 			? 0.0 : notary.doubleValue();
		double dAgency 				= null == agency 			? 0.0 : agency.doubleValue();
		double dValuation 			= null == valuation 		? 0.0 : valuation.doubleValue();
		double dCourt 				= null == court 			? 0.0 : court.doubleValue();
		return dLandAcquisition + dNotary + dAgency + dValuation + dCourt;
	}
}
