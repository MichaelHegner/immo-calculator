package ch.hemisoft.immo.domain;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PUBLIC;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString(of={"nameOfInstitution", "property"})
@EqualsAndHashCode(of={"nameOfInstitution", "property"})
@NoArgsConstructor(access=PUBLIC)
public class Credit {
	@Id @ GeneratedValue 								Long 		id;
	@Size(min=1, max=255)								String		nameOfInstitution;
	@Min(0)												Double 		interestRateNominalInPercent 			= 0.0;
	@Min(0)												Double 		redemptionAtBeginInPercent 				= 0.0;
	@Min(0)												Double 		specialRedemptionEachYearInPercent 		= 0.0;
	
	@OneToOne(mappedBy="selectedCredit", fetch=LAZY)	Property 	property;
	
	public double getTerm() {
		double financialNeedsTotal = property.getFinancialNeedsTotal();
		double rateEachYear = getRateEachYear() + getSpecialRedemptionEachYearAbsolut();
		return (Math.log(rateEachYear) - Math.log(rateEachYear - financialNeedsTotal * interestRateNominalAsQuote())) / Math.log(1 + interestRateNominalAsQuote());
	}
	
	public double getRateEachMonth() {
		return getRateEachYear() / 12;
	}

	private double getRateEachYear() {
		return property.getFinancialNeedsTotal() * sumTilgungAndInterestAsQuote();
	}
	
	private double sumTilgungAndInterestAsQuote() {
		return sumTilgungAndInterestInPercent() / 100;
	}

	private double sumTilgungAndInterestInPercent() {
		return interestRateNominalInPercent + redemptionAtBeginInPercent;
	}
	
	private double redemptionAtBeginAsQuote() {
		return redemptionAtBeginInPercent / 100;
	}
	
	private double interestRateNominalAsQuote() {
		return interestRateNominalInPercent / 100;
	}
	
	public double getSpecialRedemptionEachYearAbsolut() {
		return property.getFinancialNeedsTotal() * specialRedemptionEachYearInPercent / 100;
	}
}
