package ch.hemisoft.immo.calc.web.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.hemisoft.immo.calc.business.service.ForecastService;
import ch.hemisoft.immo.calc.business.service.PropertyService;
import ch.hemisoft.immo.calc.web.dto.ForecastDto;
import ch.hemisoft.immo.domain.Forecast;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("forecast")
@RequiredArgsConstructor
public class ForecastController {
	@NonNull final ForecastService forecastService;
	@NonNull final PropertyService propertyService;
	
	@GetMapping("/list")
	public String edit(Principal principal, ModelMap modelMap) {
		List<Property> properties = propertyService.findAll(principal);
		List<Forecast> forecasts = forecastService.findAll(principal, properties);
		modelMap.addAttribute("forecast", new ForecastDto(forecasts));
		return "forecast/list";
	}
}
