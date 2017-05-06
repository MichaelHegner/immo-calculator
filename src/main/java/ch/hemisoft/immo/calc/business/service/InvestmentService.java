package ch.hemisoft.immo.calc.business.service;

import ch.hemisoft.immo.domain.Property;

public interface InvestmentService {
	Property deactivateCredit(long propertyId);
	Property activateCredit(long propertyId, long idOfCreditToActivate);
}