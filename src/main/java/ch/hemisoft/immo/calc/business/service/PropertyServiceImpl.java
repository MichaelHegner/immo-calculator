package ch.hemisoft.immo.calc.business.service;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Service;

import ch.hemisoft.immo.calc.backend.repository.PropertyRepository;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {
	@NonNull PropertyRepository repository;
	
	@Override
	public List<Property> findAll(Principal principal) {
		return repository.findAll();
	}
	
	@Override
	public Property find(Principal principal, Long id) {
		return repository.findOne(id);
	}

	@Override
	public Property save(Principal principal, Property property) {
		return repository.save(property);
	}
}