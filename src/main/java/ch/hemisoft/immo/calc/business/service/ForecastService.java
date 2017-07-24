package ch.hemisoft.immo.calc.business.service;

import java.util.List;

import ch.hemisoft.immo.calc.business.service.dto.ForecastDto;
import ch.hemisoft.immo.domain.Property;

public interface ForecastService {
	List<ForecastDto> findAll(List<Property> properties);
	List<ForecastDto> findAll(Property property);
}
