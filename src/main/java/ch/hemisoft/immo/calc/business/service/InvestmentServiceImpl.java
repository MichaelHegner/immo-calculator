package ch.hemisoft.immo.calc.business.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ch.hemisoft.immo.calc.backend.repository.PropertyRepository;
import ch.hemisoft.immo.calc.business.utils.CreditConverter;
import ch.hemisoft.immo.domain.ActiveCredit;
import ch.hemisoft.immo.domain.NotActiveCredit;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InvestmentServiceImpl implements InvestmentService {
	@NonNull private final PropertyRepository propertyRepository;

	public Property deactivateCredit(long propertyId) {
		Property property = propertyRepository.findOne(propertyId);
		deactivateCredit(property);
		return property;
	}
	
	public Property activateCredit(long propertyId, long idOfCreditToActivate) {
		Property property = propertyRepository.findOne(propertyId);
		
		if(null == property.getSelectedCredit()) {
			activate(idOfCreditToActivate, property);
		} else if(!property.getSelectedCredit().getId().equals(idOfCreditToActivate)) {
			deactivateCredit(propertyId);
			activate(idOfCreditToActivate, property);
		}
	
		return property; 
	}

	//
	
	private void activate(long idOfCreditToActivate, Property property) {
		Optional<NotActiveCredit> creditToActivate = property.getCreditOptions().stream()
				.filter(credit -> credit.getId() == idOfCreditToActivate)
				.findAny()
				;
		
		if(creditToActivate.isPresent()) {
			property.getCreditOptions().remove(creditToActivate.get());
			property.setSelectedCredit(CreditConverter.convert(creditToActivate.get()));
		}
	}
	
	private void deactivateCredit(Property property) {
		ActiveCredit selectedCredit = property.getSelectedCredit();
		if (null != selectedCredit) {
			property.setSelectedCredit(null);
			property.getCreditOptions().add(CreditConverter.convert(selectedCredit));
		}
	}
}