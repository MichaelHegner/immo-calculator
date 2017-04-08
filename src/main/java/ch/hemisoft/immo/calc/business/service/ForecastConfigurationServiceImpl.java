package ch.hemisoft.immo.calc.business.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ch.hemisoft.immo.calc.backend.repository.ForecastConfigurationRepository;
import ch.hemisoft.immo.domain.ForecastConfiguration;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ForecastConfigurationServiceImpl implements ForecastConfigurationService {
	@NonNull private ForecastConfigurationRepository repository;

	@Override
	public List<ForecastConfiguration> findAll() {
		 return repository.findAll();
	}

}
