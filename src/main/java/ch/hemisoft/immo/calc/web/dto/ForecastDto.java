package ch.hemisoft.immo.calc.web.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import ch.hemisoft.immo.domain.Forecast;
import ch.hemisoft.immo.utils.ListStreamUtils;
import lombok.Data;

@Data
public class ForecastDto {
	private final int FORECAST_IN_YEARS = 10;
	
	private final List<Forecast> forecasts;

	public ForecastDto(List<Forecast> forecasts) {
		this.forecasts = forecasts;
	}
	
	
	public List<Integer> getYears() {
		return forecasts.stream().map(f -> f.getYear()).collect(Collectors.toList());
	}
	
	public List<BigDecimal> getRentalForecast() {
		return forecasts.stream().map(f -> f.getIncomeBeforeCost()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumRentalForecast() {
		return ListStreamUtils.sumBigDecimal(this::getRentalForecast);
	}
	
	public List<BigDecimal> getRunningCostForecast() {
		return forecasts.stream().map(f -> f.getRunningCost()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumRunningCostForecast() {
		return ListStreamUtils.sumBigDecimal(this::getRunningCostForecast);
	}

	public List<BigDecimal> getSpecialCostForecast() {
		return forecasts.stream().map(f -> f.getSpecialCost()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumSpecialCostForecast() {
		return ListStreamUtils.sumBigDecimal(this::getSpecialCostForecast);
	}
	
	public List<BigDecimal> getIncomeAfterCostForecast() {
		return forecasts.stream().map(f -> f.getIncomeAfterCost()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumIncomeAfterCostForecast() {
		return ListStreamUtils.sumBigDecimal(this::getIncomeAfterCostForecast);
	}	
	
	public List<BigDecimal> getInterestForecast() {
		return forecasts.stream().map(f -> f.getInterest()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumInterestForecast() {
		return ListStreamUtils.sumBigDecimal(this::getInterestForecast);
	}
	
	public List<BigDecimal> getOperationResultForecast() {
		return forecasts.stream().map(f -> f.getOperationResult()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumOperationResultForecast() {
		return ListStreamUtils.sumBigDecimal(this::getOperationResultForecast);
	}
	
	public List<BigDecimal> getDeprecationForecast() {
		return forecasts.stream().map(f -> f.getDeprecation()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumDeprecationForecast() {
		return ListStreamUtils.sumBigDecimal(this::getDeprecationForecast);
	}	
	
	public List<BigDecimal> getResultBeforeTaxForecast() {
		return forecasts.stream().map(f -> f.getResultBeforeTax()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumResultBeforeTaxForecast() {
		return ListStreamUtils.sumBigDecimal(this::getResultBeforeTaxForecast);
	}	
	
	public List<BigDecimal> getRoiBeforeTaxForecast() {
		return forecasts.stream().map(f -> f.getRoiBeforeTax()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumRoiBeforeTaxForecast() {
		return ListStreamUtils.sumBigDecimal(this::getRoiBeforeTaxForecast);
	}	
	
	public List<BigDecimal> getTaxForecast() {
		return forecasts.stream().map(f -> f.getTax()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumTaxForecast() {
		return ListStreamUtils.sumBigDecimal(this::getTaxForecast);
	}
	
	public List<BigDecimal> getResultAfterTaxForecast() {
		return forecasts.stream().map(f -> f.getResultAfterTax()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumResultAfterTaxForecast() {
		return ListStreamUtils.sumBigDecimal(this::getResultAfterTaxForecast);
	}
	
	public List<BigDecimal> getRoiAfterTaxForecast() {
		return forecasts.stream().map(f -> f.getRoiAfterTax()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumRoiAfterTaxForecast() {
		return ListStreamUtils.sumBigDecimal(this::getRoiAfterTaxForecast);
	}
	
	public List<BigDecimal> getRedemptionForecast() {
		return forecasts.stream().map(f -> f.getRedemption()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumRedemptionForecast() {
		return ListStreamUtils.sumBigDecimal(this::getRedemptionForecast);
	}
}
