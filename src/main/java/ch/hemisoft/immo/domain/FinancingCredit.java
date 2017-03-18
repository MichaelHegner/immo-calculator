package ch.hemisoft.immo.domain;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class FinancingCredit {
	Double interestRateNominalInPercent;
	Double redemptionAtBeginInPercent;
	Double specialRedemptionEachYearInPercent;
	Double specialRedemptionEachYearAbsolut;
}
