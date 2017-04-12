package ch.hemisoft.immo.domain;

import static javax.persistence.FetchType.LAZY;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import ch.hemisoft.immo.utils.BigDecimalUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(uniqueConstraints={
    @UniqueConstraint(columnNames = {"year", "property_id"})
}) 
@Data
@ToString(of={"year", "property"})
@EqualsAndHashCode(of={"year", "property"})
@NoArgsConstructor
@RequiredArgsConstructor
public class Forecast {
	@Id @GeneratedValue
	private Long id;
	
	@NotNull @NonNull
	@OneToOne(fetch=LAZY)	
	private Property property;
	
	@NonNull @NotNull
	private Integer year;
	
	@NonNull @NotNull
	@Column(precision=10, scale=2)
	private BigDecimal incomeBeforeCost;

	@NonNull @NotNull
	@Column(precision=10, scale=2)
	private BigDecimal runningCost;
	
	@NonNull @NotNull
	@Column(precision=10, scale=2)
	private BigDecimal specialCost;
	
	@NonNull @NotNull
	@Column(precision=10, scale=2)
	private BigDecimal interest;
	
	public Forecast(int year) {
		this(null, year);
	}	
	
	public Forecast(Property property, int year) {
		this.property = property;
		this.year = year;
		incomeBeforeCost = BigDecimalUtils.convert(0.0);
		runningCost = BigDecimalUtils.convert(0.0);
		specialCost = BigDecimalUtils.convert(0.0);
		interest = BigDecimalUtils.convert(0.0);
	}
	
	//
	
	public BigDecimal getIncomeAfterCost() {
		return incomeBeforeCost.subtract(runningCost).subtract(specialCost);
	}
	
	public BigDecimal getOperationResult() {
		return getIncomeAfterCost().subtract(interest);
	}
}


