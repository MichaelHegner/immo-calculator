package ch.hemisoft.immo.calc.business.service.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import ch.hemisoft.immo.utils.BigDecimalUtils;
import groovy.transform.EqualsAndHashCode;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode(includes = "year")
public class ForecastDto {
	@NonNull @NotNull		private Integer 				year;
	@NotNull @NonNull		private BigDecimal 				netAssets;
	@NonNull @NotNull		private BigDecimal 				incomeBeforeCost 	= BigDecimalUtils.convert(0.0);
	@NonNull @NotNull		private BigDecimal 				runningCost 		= BigDecimalUtils.convert(0.0);
	@NonNull @NotNull		private BigDecimal 				specialCost			= BigDecimalUtils.convert(0.0);
	@NonNull @NotNull		private BigDecimal 				interest			= BigDecimalUtils.convert(0.0);
	@NonNull @NotNull		private BigDecimal 				deprecation			= BigDecimalUtils.convert(0.0);
	@NonNull @NotNull		private BigDecimal 				redemption			= BigDecimalUtils.convert(0.0);
	@NonNull @NotNull		private BigDecimal 				taxQuote			= BigDecimalUtils.convert(0.0);
							private Boolean 				german;
	
	
	public ForecastDto(int year, BigDecimal netAssets, BigDecimal taxQuote, Boolean german) {
		this.year = year;
		this.netAssets = netAssets;
		this.taxQuote = taxQuote;
		this.german = german;
	}
	
	//
	
	public BigDecimal getIncomeAfterCost() {
		return incomeBeforeCost.subtract(runningCost).subtract(specialCost);
	}
	
	public BigDecimal getOperationResult() {
		return getIncomeAfterCost().subtract(interest);
	}
	
	public BigDecimal getResultBeforeTax() {
		return getOperationResult().subtract(getDeprecation());
	}
	
	public BigDecimal getRoiBeforeTax() {
		return BigDecimalUtils.convert(getResultBeforeTax().doubleValue() / netAssets.doubleValue() * 100);
	}
	
	public BigDecimal getTax() {
		return BigDecimalUtils.convert(getResultBeforeTax().doubleValue() * (taxQuote.doubleValue() / 100) * ((german) ? 1 : 1.055));
	}
	
	public BigDecimal getResultAfterTax() {
		return getResultBeforeTax().subtract(getTax());
	}
	
	public BigDecimal getRoiAfterTax() {
		return BigDecimalUtils.convert(getResultAfterTax().doubleValue() / netAssets.doubleValue() * 100);
	}
	
	public BigDecimal getResultAfterRedemption() {
		return getResultBeforeTax().subtract(getTax()).subtract(redemption);
	}
	
	public BigDecimal getCashFlow() {
		return getResultAfterRedemption().add(getDeprecation());
	}
}


