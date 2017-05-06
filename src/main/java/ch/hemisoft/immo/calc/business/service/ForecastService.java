package ch.hemisoft.immo.calc.business.service;

import java.util.List;

import ch.hemisoft.immo.domain.Forecast;
import ch.hemisoft.immo.domain.Property;

public interface ForecastService {
	void save(Property property);
	List<Forecast> findAll(List<Property> properties);
	List<Forecast> findAll(Property property);
}
