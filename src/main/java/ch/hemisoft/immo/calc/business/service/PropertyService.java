package ch.hemisoft.immo.calc.business.service;

import java.security.Principal;
import java.util.List;

import ch.hemisoft.immo.domain.Property;

public interface PropertyService {
	List<Property> findAll(Principal principal);
	Property find(Principal principal, Long id);
	Property save(Principal principal, Property property);
}