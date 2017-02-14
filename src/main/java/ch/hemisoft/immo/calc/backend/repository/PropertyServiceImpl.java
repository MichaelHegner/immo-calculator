package ch.hemisoft.immo.calc.backend.repository;

import org.springframework.stereotype.Service;

import ch.hemisoft.immo.calc.business.service.PropertyService;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {
	@NonNull PropertyRepository repository;

	@Override
	public Property save(Property property) {
		return repository.save(property);
	}
}