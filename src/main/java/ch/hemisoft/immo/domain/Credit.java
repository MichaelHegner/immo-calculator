package ch.hemisoft.immo.domain;

import static lombok.AccessLevel.PACKAGE;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor(access=PACKAGE)
public class Credit {
	@Id @ GeneratedValue 	Long 	id;
	@Min(0)					double 	interestRateNominalInPercent;
	@Min(0)					double 	redemptionAtBeginInPercent;
	@Min(0)					double 	specialRedemptionEachYearInPercent;
	@Min(0)					double 	specialRedemptionEachYearAbsolut;
}
