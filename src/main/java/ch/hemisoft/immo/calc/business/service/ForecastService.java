package ch.hemisoft.immo.calc.business.service;

import java.security.Principal;

import ch.hemisoft.immo.calc.web.dto.ForecastDto;

public interface ForecastService {
	ForecastDto findAll(Principal principal);
}
