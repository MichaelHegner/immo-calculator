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
	@Min(0)												double 		interestRateNominalInPercent;
	@Min(0)												double 		redemptionAtBeginInPercent;
	@Min(0)												double 		specialRedemptionEachYearInPercent;
	@Min(0)												double 		specialRedemptionEachYearAbsolut;
	
	@OneToOne(mappedBy="selectedCredit", fetch=LAZY)	Property 	property;
	
	public double getMonthlyRate() {
		double sumTilgungAndInterestQuote = (interestRateNominalInPercent + redemptionAtBeginInPercent) / 100;
		return property.getFinancialNeedsTotal() * sumTilgungAndInterestQuote / 12;
	}
}
