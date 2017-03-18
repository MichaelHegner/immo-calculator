package ch.hemisoft.immo.domain;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class FinancingCredit {
	double interestRateNominalInPercent;
	double redemptionAtBeginInPercent;
	double specialRedemptionEachYearInPercent;
	double specialRedemptionEachYearAbsolut;
}
