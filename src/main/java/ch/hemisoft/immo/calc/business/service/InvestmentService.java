package ch.hemisoft.immo.calc.business.service;

import java.security.Principal;

import ch.hemisoft.immo.domain.Property;

public interface InvestmentService {
	Property deactivateCredit(Principal principal, Long propertyId);
}