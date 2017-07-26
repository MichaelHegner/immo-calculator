package ch.hemisoft.immo.calc.business.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ch.hemisoft.commons.CollectionUtils;
import ch.hemisoft.immo.calc.business.service.vo.ForecastVO;
import ch.hemisoft.immo.calc.business.utils.ForecastCalculator;
import ch.hemisoft.immo.domain.Credit;
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
	@NonNull private CostPlanningService costPlanningService;
	@NonNull private ForecastConfigurationService forecastConfigurationService;
	
	@Override
	public List<ForecastVO> findAll(List<Property> properties) {

		// PREPARE ...
		List<ForecastVO> forecasts = new ArrayList<>(FORECAST_TERM);
		Map<String, ForecastConfiguration> forecastConfigurationMap = createForecastConfigurationMap();
		
		// POPULATE ...
		for(Property property : properties) {
			final boolean firstProperty = properties.indexOf(property) <= 0;
			
			
			final int yearToday = LocalDate.now().get(ChronoField.YEAR);
			List<ForecastVO> findAll = findAll(property);
			for(int i = 0; i < FORECAST_TERM; i++) {
				final int yearLoop = i + yearToday;
				
				final ForecastVO forecastAtI;
				if(firstProperty) {
					final BigDecimal netAssets = BigDecimalUtils.convert(property.getNetAssets());
					final String countryCode = property.getAddress().getCountryCode();
					final BigDecimal taxQuote = BigDecimalUtils.convert(forecastConfigurationMap.get(countryCode).getTaxQuote());
					final boolean german = "DE".equals(countryCode);
					forecastAtI = new ForecastVO(yearLoop, netAssets, taxQuote, german);
				} else {
					forecastAtI = forecasts.get(i);
				}
				
				forecastAtI.addIncomeBeforeCost(findAll.get(i).getIncomeBeforeCost());
				forecastAtI.addRunningCost(findAll.get(i).getRunningCost());
				forecastAtI.addSpecialCost(CollectionUtils.reduce(costPlanningService.findAll(property, yearLoop), t -> t.getAmount()));
				forecastAtI.addInterest(findAll.get(i).getInterest());
				forecastAtI.addDeprecation(findAll.get(i).getDeprecation());
				forecastAtI.addRedemption(findAll.get(i).getRedemption());
				
				if(firstProperty) {
					forecasts.add(forecastAtI);
				}
			}
		}
		
		return forecasts;
	}

	
	@Override
	public List<ForecastVO> findAll(Property property) {
		List<ForecastVO> forecasts = new ArrayList<>();
		String countryCode = property.getAddress().getCountryCode();
		ForecastConfiguration configuration = forecastConfigurationService.findByCountryCode(countryCode);
		
		final int yearToday = LocalDate.now().get(ChronoField.YEAR);
		for(int i = 0; i < FORECAST_TERM; i++) {
			final int yearLoop = i + yearToday;
			final BigDecimal netAssets = BigDecimalUtils.convert(property.getNetAssets());
			final BigDecimal taxQuote = BigDecimalUtils.convert(configuration.getTaxQuote());
			final boolean german = "DE".equals(countryCode);
			ForecastVO forecast = new ForecastVO(yearLoop, netAssets, taxQuote, german);
			populateRental(property, forecast, configuration.getRentalIncrease(), configuration.getRentalIncreaseFrequence());
			populateRunningCost(property, forecast, configuration.getRunningCostIndex(), 1);
			
			
			for (Credit credit : property.getActiveCredits()) {
				populateInterest(property, credit, forecast);
			}
			
			populateDeprecation(property, forecast, configuration);

			for (Credit credit : property.getActiveCredits()) {
				populateRedemption(property, credit, forecast);
			}
			
			forecasts.add(forecast);
		}
		return forecasts;
	}
	
	//
	
	private Map<String, ForecastConfiguration> createForecastConfigurationMap() {
		return forecastConfigurationService.findAll().stream().collect(Collectors.toMap(ForecastConfiguration::getCountryCode, Function.identity()));
	}

	//

	private void populateRental(Property property, ForecastVO forecast, double percentalIncrease, int increaseFrequence) {
		double value = (null == property.getRentalNet()) ? 0.0 : property.getRentalNet().doubleValue();
		double i = percentalIncrease / 100; 
		long yearDiff = ForecastCalculator.calculatePropertyForecastOfYear(property, forecast);
		double q = 1 + i;
		double t = yearDiff % increaseFrequence == 0 ? yearDiff :  yearDiff - yearDiff % increaseFrequence;
		double qt = Math.pow(q, t);
		double result = value * qt;
		forecast.setIncomeBeforeCost(BigDecimalUtils.convert(result));
	}

	private void populateRunningCost(Property property, ForecastVO forecast, Double percentalIncrease, int increaseFrequence) {
		double value = property.getTotalManagementCost().doubleValue();
		double i = percentalIncrease / 100;
		long yearDiff = ForecastCalculator.calculatePropertyForecastOfYear(property, forecast); // TODO: CHECK -1
		double q = 1 + i;
		double t = yearDiff % increaseFrequence == 0 ? yearDiff :  yearDiff - yearDiff % increaseFrequence;
		double qt = Math.pow(q, t);
		double result = value * qt;
		forecast.setRunningCost(BigDecimalUtils.convert(result));
	}
	
	private void populateInterest(Property property, Credit credit, ForecastVO forecast) {
		int yearDiff = ForecastCalculator.calculatePropertyForecastAtEndOfYear(property, forecast);
		double result = ForecastCalculator.calculateInterest(credit, yearDiff);
		forecast.setInterest(forecast.getInterest().add(BigDecimalUtils.convert(result)));
	}
	
	private void populateDeprecation(Property property, ForecastVO forecast, ForecastConfiguration configuration) {
		forecast.setDeprecation(BigDecimalUtils.convert(ForecastCalculator.calculateDeprecation(property, configuration)));
	}
	
	private void populateRedemption(Property property, Credit credit, ForecastVO forecast) {
		int forecastYear = forecast.getYear();
		int yearPurchaseProperty = property.getPurchaseDate().get(ChronoField.YEAR);
		
		double redemption;
		int termSelectedCredit = credit.getTerm().intValue();
		if (forecastYear - yearPurchaseProperty <= termSelectedCredit) {
			int yearDiff = ForecastCalculator.calculatePropertyForecastAtEndOfYear(property, forecast);
			redemption = ForecastCalculator.calculateRedemption(property, credit, forecast, yearDiff);
		} else {
			redemption = 0.0;
		}

		forecast.setRedemption(forecast.getRedemption().add(BigDecimalUtils.convert(redemption)));
	}


	
}
