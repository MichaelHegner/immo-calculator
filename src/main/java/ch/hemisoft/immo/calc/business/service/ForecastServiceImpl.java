package ch.hemisoft.immo.calc.business.service;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import ch.hemisoft.immo.calc.web.dto.ForecastConfigurationDto;
import ch.hemisoft.immo.calc.web.dto.ForecastDto;
import ch.hemisoft.immo.domain.ForecastConfiguration;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ForecastServiceImpl implements ForecastService {
	@NonNull private PropertyService propertyService;
	@NonNull private ForecastConfigurationService forecastConfigurationService;

	@Override
	public ForecastDto findAll(Principal principal) {
		List<ForecastConfiguration> forecastConfigurations = forecastConfigurationService.findAll();
		ForecastDto forecast = new ForecastDto(getPopulatedForeastConfigurationDtos(forecastConfigurations));
		
		List<Property> boughtProperties = propertyService.findAllBought(principal);
		for(Property property : boughtProperties) {
			String countryCode = property.getAddress().getCountryCode();
			forecast.addRental(countryCode, property.getRentalNet());
			forecast.addRunningCost(countryCode, property.getTotalManagementCost());
		}
		
		return forecast;
	}

	private Map<String, ForecastConfigurationDto> getPopulatedForeastConfigurationDtos(List<ForecastConfiguration> forecastConfigurations) {
		Map<String, ForecastConfigurationDto> map = new HashMap<>();
		
		for(ForecastConfiguration persistentConfig : forecastConfigurations) {
			ForecastConfigurationDto dto = new ForecastConfigurationDto();
			BeanUtils.copyProperties(persistentConfig, dto);
			map.put(dto.getCountryCode(), dto);
		}

		return map;
	}

}
