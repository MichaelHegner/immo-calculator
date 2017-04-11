package ch.hemisoft.immo.calc.business.service;

import java.security.Principal;
import java.util.List;

import ch.hemisoft.immo.domain.Forecast;
import ch.hemisoft.immo.domain.Property;

public interface ForecastService {
	void save(Property property);
	List<Forecast> findAll(Principal principal, List<Property> properties);
	List<Forecast> findAll(Principal principal, Property property);
}
