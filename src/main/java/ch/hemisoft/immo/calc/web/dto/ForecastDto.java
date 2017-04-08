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
import lombok.Data;

@Data
public class ForecastDto {
	private final int FORECAST_IN_YEARS = 10;
	
	private final Map<String, ForecastConfigurationDto> configuration;
	public ForecastDto(Map<String, ForecastConfigurationDto> configuration) {
		this.configuration = configuration;
	}
	
	private final Map<String, BigDecimal> rentals = new HashMap<>(); // County, Value
	public void addRental(String country, BigDecimal rental) {
		Assert.notNull(country, "Country must not be null.");
		Assert.notNull(rental, "Rental must not be null.");
		if(!rentals.containsKey(country)) rentals.put(country, BigDecimal.valueOf(0.0));
		rentals.compute(country, (k, v) -> v = v.add(rental));
	}
	
	public List<Integer> getYears() {
		return IntStream.range(1, FORECAST_IN_YEARS + 1).boxed().collect(Collectors.toList());
	}
	
	public List<BigDecimal> getRentalForecast() {
		List<BigDecimal> resultList = new ArrayList<>(FORECAST_IN_YEARS);
		
		Map<String, BigDecimal> currentRentals = new HashMap<>(rentals);
		for(int index = 0; index < FORECAST_IN_YEARS; index++) {
			for(String country : rentals.keySet()) {
				if(index != 0 && index % 2 == 0) {
					currentRentals.compute(country, (k, v) -> v = v.multiply(BigDecimal.valueOf(1 + configuration.get(country).getRentalIncreaseAllTwoYears() / 100)));
				}
				
				BigDecimal valueOfCountry = BigDecimalUtils.convert(currentRentals.get(country).doubleValue());
				
				if(resultList.size() <= index) {
					resultList.add(valueOfCountry);
				} else {
					resultList.set(index, valueOfCountry.add(resultList.get(index)));
				}
			}
		}
		
		return resultList;
	}
}
