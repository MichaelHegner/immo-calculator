package ch.hemisoft.immo.calc.business.service;

import java.security.Principal;

import org.springframework.stereotype.Service;

import ch.hemisoft.immo.calc.backend.repository.NotActiveCreditRepository;
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
	@NonNull private final NotActiveCreditRepository notActiveCreditRepository;

	@Override
	public Property deactivateCredit(Principal principal, Long propertyId) {
		Property property = propertyRepository.findOne(propertyId);
		ActiveCredit selectedCredit = property.getSelectedCredit();
		NotActiveCredit notActiveCredit = notActiveCreditRepository.save(CreditConverter.convert(selectedCredit));
		property.setSelectedCredit(null);
		return property;
	}
}