package ch.hemisoft.immo.calc.business.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ch.hemisoft.immo.calc.backend.repository.PropertyRepository;
import ch.hemisoft.immo.domain.Property;
import ch.hemisoft.immo.domain.PropertyStatus;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {
	@NonNull PropertyRepository repository;
	
	@Override
	public List<Property> findAll() {
		return repository.findAll();
	}

	@Override
	public List<Property> findAllBought() {
		return repository.findAllByStatus(PropertyStatus.BOUGHT);
	}

	@Override
	public List<Property> findAllConcrete() {
		return repository.findAllByStatusIn(PropertyStatus.BOUGHT, PropertyStatus.PROPOSED);
	}
	
	@Override
	public Property find(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	public Property save(Property property) {
		return repository.save(property);
	}

	@Override
	public void deleteById(long id) {
		repository.deleteById(id);
	}
}