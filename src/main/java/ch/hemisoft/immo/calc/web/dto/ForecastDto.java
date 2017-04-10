package ch.hemisoft.immo.calc.web.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.util.Assert;

import ch.hemisoft.immo.utils.BigDecimalUtils;
import ch.hemisoft.immo.utils.ListStreamUtils;
import lombok.Data;

@Data
public class ForecastDto {
	private final int FORECAST_IN_YEARS = 10;
	
	private final Map<String, ForecastConfigurationDto> configuration;
	public ForecastDto(Map<String, ForecastConfigurationDto> configuration) {
		this.configuration = configuration;
	}
	
	
	public List<Integer> getYears() {
		return IntStream.range(1, FORECAST_IN_YEARS + 1).boxed().collect(Collectors.toList());
	}
	
	// RENTALS ...

	private final Map<String, BigDecimal> rentals = new HashMap<>(); // County, Value
	public void addRental(String country, BigDecimal rental) {
		Assert.notNull(country, "Country must not be null.");
		Assert.notNull(rental, "Rental must not be null.");
		if(!rentals.containsKey(country)) rentals.put(country, BigDecimal.valueOf(0.0));
		rentals.compute(country, (k, v) -> v = v.add(rental));
	}
	
	public List<BigDecimal> getRentalForecast() {
		return getPopulateRentalForecast(rentals);
	}

	private List<BigDecimal> getPopulateRentalForecast(Map<String, BigDecimal> values) {
		List<BigDecimal> resultList = new ArrayList<>(FORECAST_IN_YEARS);
		Map<String, BigDecimal> currentValue = new HashMap<>(values);
		for(int index = 0; index < FORECAST_IN_YEARS; index++) {
			for(String country : values.keySet()) {
				populateRentalForecast(resultList, currentValue, index, country);
			}
		}
		return resultList;
	}

	private void populateRentalForecast(List<BigDecimal> resultList, Map<String, BigDecimal> currentValue, int index, String country) {
		if(index != 0 && index % 2 == 0) {
			currentValue.compute(country, (k, v) -> v = v.multiply(BigDecimal.valueOf(1 + configuration.get(country).getRentalIncreaseAllTwoYears() / 100)));
		}
		
		BigDecimal valueOfCountry = BigDecimalUtils.convert(currentValue.get(country).doubleValue());
		
		if(resultList.size() <= index) {
			resultList.add(valueOfCountry);
		} else {
			resultList.set(index, valueOfCountry.add(resultList.get(index)));
		}
	}
	
	public BigDecimal getSumRentalForecast() {
		return getRentalForecast().stream().reduce(BigDecimal.valueOf(0), (l, r) -> l.add(r));
	}
	
	//
	
	// RUNNING COSTS ...
	
	private final Map<String, BigDecimal> runningCosts = new HashMap<>(); // County, Value
	public void addRunningCost(String country, BigDecimal rental) {
		Assert.notNull(country, "Country must not be null.");
		Assert.notNull(rental, "Rental must not be null.");
		if(!runningCosts.containsKey(country)) runningCosts.put(country, BigDecimal.valueOf(0.0));
		runningCosts.compute(country, (k, v) -> v = v.add(rental));
	}
	
	public List<BigDecimal> getRunningCostForecast() {
		return getRunningCostForecast(runningCosts);
	}

	private List<BigDecimal> getRunningCostForecast(Map<String, BigDecimal> values) {
		List<BigDecimal> resultList = new ArrayList<>(FORECAST_IN_YEARS);
		Map<String, BigDecimal> currentValue = new HashMap<>(values);
		for(int index = 0; index < FORECAST_IN_YEARS; index++) {
			for(String country : values.keySet()) {
				populateRunningCostForecast(resultList, currentValue, index, country);
			}
		}
		return resultList;
	}
	
	private void populateRunningCostForecast(List<BigDecimal> resultList, Map<String, BigDecimal> currentValue, int index, String country) {
		currentValue.compute(country, (k, v) -> v = v.multiply(BigDecimal.valueOf(1 + configuration.get(country).getRunningCostIndex() / 100)));

		BigDecimal valueOfCountry = BigDecimalUtils.convert(currentValue.get(country).doubleValue());
		if(resultList.size() <= index) {
			resultList.add(valueOfCountry);
		} else {
			resultList.set(index, valueOfCountry.add(resultList.get(index)));
		}
	}
	
	public BigDecimal getSumRunningCostForecast() {
		return ListStreamUtils.sumBigDecimal(() -> getRunningCostForecast());
	}
	
	// 
	
	// SPECIAL COSTS ...
	
	private final Map<String, BigDecimal> specialCosts = new HashMap<>(); // County, Value
	public void addSpecialCost(String country, BigDecimal rental) {
		Assert.notNull(country, "Country must not be null.");
		Assert.notNull(rental, "Rental must not be null.");
		if(!specialCosts.containsKey(country)) specialCosts.put(country, BigDecimal.valueOf(0.0));
		specialCosts.compute(country, (k, v) -> v = v.add(rental));
	}
	
	public List<BigDecimal> getSpecialCostForecast() {
		return getSpecialCostForecast(specialCosts);
	}
	
	private List<BigDecimal> getSpecialCostForecast(Map<String, BigDecimal> values) {
		List<BigDecimal> resultList = new ArrayList<>(FORECAST_IN_YEARS);
		Map<String, BigDecimal> currentValue = new HashMap<>(values);
		for(int index = 0; index < FORECAST_IN_YEARS; index++) {
			for(String country : values.keySet()) {
				populateSpecialCostForecast(resultList, currentValue, index, country);
			}
		}
		return resultList;
	}

	private void populateSpecialCostForecast(List<BigDecimal> resultList, Map<String, BigDecimal> currentValue, int index, String country) {
		currentValue.compute(country, (k, v) -> v);

		BigDecimal valueOfCountry = BigDecimalUtils.convert(currentValue.get(country).doubleValue());
		if(resultList.size() <= index) {
			resultList.add(valueOfCountry);
		} else {
			resultList.set(index, valueOfCountry.add(resultList.get(index)));
		}
	}
	
	public BigDecimal getSumSpecialCostForecast() {
		return ListStreamUtils.sumBigDecimal(() -> getSpecialCostForecast());
	}
}
