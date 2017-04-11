package ch.hemisoft.immo.calc.business.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ch.hemisoft.immo.calc.backend.repository.ForecastRepository;
import ch.hemisoft.immo.domain.Forecast;
import ch.hemisoft.immo.domain.ForecastConfiguration;
import ch.hemisoft.immo.domain.Property;
import ch.hemisoft.immo.utils.BigDecimalUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ForecastServiceImpl implements ForecastService {
	private static final int FORECAST_TERM = 10;
	
	@NonNull private PropertyService propertyService;
	@NonNull private ForecastConfigurationService forecastConfigurationService;
	@NonNull private ForecastRepository forecastRepository;
	

	@Override
	public List<Forecast> findAll(Principal principal, List<Property> properties) {
		properties.forEach(this::save); // TODO SOC

		List<Forecast> forecasts = new ArrayList<>(FORECAST_TERM);
		for(int i = 0; i < FORECAST_TERM; i++) forecasts.add(new Forecast(i));
		
		for(Property property : properties) {
			List<Forecast> findAll = findAll(principal, property);
			for(int i = 0; i < FORECAST_TERM; i++) {
				forecasts.get(i).setYear(i + 1);
				forecasts.get(i).setIncomeBeforeCost(forecasts.get(i).getIncomeBeforeCost().add(findAll.get(i).getIncomeBeforeCost()));
				forecasts.get(i).setRunningCost(forecasts.get(i).getRunningCost().add(findAll.get(i).getRunningCost()));
				forecasts.get(i).setSpecialCost(forecasts.get(i).getSpecialCost().add(findAll.get(i).getSpecialCost()));
//				forecasts.get(i).setIncomeAfterCost(forecasts.get(i).getIncomeAfterCost().add(findAll.get(i).getIncomeAfterCost()));
			}
		}
		
		return forecasts;
	}
	
	@Override
	public List<Forecast> findAll(Principal principal, Property property) {
		save(property); // TODO SOC
		return forecastRepository.findAllByPropertyOrderByYearAsc(property);
	}

	@Override
	public void save(Property property) {
		String countryCode = property.getAddress().getCountryCode();
		ForecastConfiguration configuration = forecastConfigurationService.findByCountryCode(countryCode);
		
		for(int i = 0; i < FORECAST_TERM; i++) {
			Forecast forecast = forecastRepository.findByYearAndProperty(i, property);
			if(null == forecast) {
				forecast = new Forecast(property, i);
				forecastRepository.save(forecast);
			}
			
			populateRental(property, forecast, configuration.getRentalIncreaseAllTwoYears(), 2);
			populateRunningCost(property, forecast, configuration.getRunningCostIndex(), 1);
		}
	}

	private void populateRental(Property property, Forecast forecast, double rentalIncrease, int increaseFrequence) {
		double value = property.getRentalNet().doubleValue();
		double i = rentalIncrease / 100;
		long currentYear = forecast.getYear();
		double q = 1 + i;
		double t = currentYear % increaseFrequence == 0 ? currentYear :  currentYear - currentYear % increaseFrequence;
		double qt = Math.pow(q, t);
		forecast.setIncomeBeforeCost(BigDecimalUtils.convert(value * qt));
	}

	private void populateRunningCost(Property property, Forecast forecast, Double runningCostIndex, int increaseFrequence) {
		double value = property.getTotalManagementCost().doubleValue();
		double i = runningCostIndex / 100;
		long currentYear = forecast.getYear();
		double q = 1 + i;
		double t = currentYear % increaseFrequence == 0 ? currentYear :  currentYear - currentYear % increaseFrequence;
		double qt = Math.pow(q, t);
		forecast.setRunningCost(BigDecimalUtils.convert(value * qt));
	}
}
