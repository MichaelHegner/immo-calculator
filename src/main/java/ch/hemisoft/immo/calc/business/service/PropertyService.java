package ch.hemisoft.immo.calc.business.service;

import java.util.List;

import ch.hemisoft.immo.domain.Property;

public interface PropertyService {
	List<Property> findAll();
	Property save(Property property);
}