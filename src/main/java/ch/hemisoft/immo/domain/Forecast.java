package ch.hemisoft.immo.domain;

import static javax.persistence.FetchType.LAZY;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
	private BigDecimal incomeBeforeCost 	= BigDecimalUtils.convert(0.0);

	@NonNull @NotNull
	@Column(precision=10, scale=2)
	private BigDecimal runningCost 			= BigDecimalUtils.convert(0.0);
	
	@NonNull @NotNull
	@Column(precision=10, scale=2)
	private BigDecimal specialCost			= BigDecimalUtils.convert(0.0);
	
	@NonNull @NotNull
	@Column(precision=10, scale=2)
	private BigDecimal interest				= BigDecimalUtils.convert(0.0);
	
	@NonNull @NotNull
	@ManyToOne(optional = false)
	private ForecastConfiguration configuration;
	
	//
	
	public Forecast(Property property, ForecastConfiguration configuration, int year) {
		this.property = property;
		this.year = year;
		this.configuration = configuration;
	}
	
	//
	
	public BigDecimal getIncomeAfterCost() {
		return incomeBeforeCost.subtract(runningCost).subtract(specialCost);
	}
	
	public BigDecimal getOperationResult() {
		return getIncomeAfterCost().subtract(interest);
	}
	
	public BigDecimal getDeprecation() {
		double value = property.getPurchasePrice().doubleValue();
		double i = configuration.getDeprecation() / 100;
		return BigDecimalUtils.convert(value * i);
	}
}


