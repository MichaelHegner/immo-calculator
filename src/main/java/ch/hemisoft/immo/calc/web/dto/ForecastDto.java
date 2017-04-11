package ch.hemisoft.immo.calc.web.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import ch.hemisoft.immo.domain.Forecast;
import ch.hemisoft.immo.utils.BigDecimalUtils;
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
		return getRentalForecast().stream().reduce(BigDecimalUtils.convert(0.0), (l, r) -> l.add(r));
	}
	
	public List<BigDecimal> getRunningCostForecast() {
		return forecasts.stream().map(f -> f.getRunningCost()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumRunningCostForecast() {
		return getRunningCostForecast().stream().reduce(BigDecimalUtils.convert(0.0), (l, r) -> l.add(r));
	}

	public List<BigDecimal> getSpecialCostForecast() {
		return forecasts.stream().map(f -> f.getSpecialCost()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumSpecialCostForecast() {
		return getSpecialCostForecast().stream().reduce(BigDecimalUtils.convert(0.0), (l, r) -> l.add(r));
	}
	
	public List<BigDecimal> getIncomeAfterCostForecast() {
		return forecasts.stream().map(f -> f.getIncomeAfterCost()).collect(Collectors.toList());
	}
	
	public BigDecimal getSumIncomeAfterCostForecast() {
		return getIncomeAfterCostForecast().stream().reduce(BigDecimalUtils.convert(0.0), (l, r) -> l.add(r));
	}
}
