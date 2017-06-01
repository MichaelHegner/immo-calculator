package ch.hemisoft.immo.calc.web.controller;

import static java.lang.Boolean.FALSE;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	public String list(ModelMap modelMap) {
		List<Property> properties = propertyService.findAll();
		List<Forecast> forecasts = forecastService.findAll(properties);
		modelMap.addAttribute("forecast", new ForecastDto(forecasts));
		modelMap.addAttribute("properties", properties);
		return "forecast/list";
	}	
	
	@GetMapping("/list/{propertyId}")
	public String list(@PathVariable Long propertyId, ModelMap modelMap) {
		Property property = propertyService.find(propertyId);
		List<Property> properties = Arrays.asList(property);
		List<Forecast> forecasts = forecastService.findAll(properties);
		modelMap.addAttribute("forecast", new ForecastDto(forecasts));
		modelMap.addAttribute("properties", propertyService.findAll());
		modelMap.addAttribute("property", property);
		return "forecast/list";
	}
	
	@PostMapping("/list/{propertyId}")
	public String save (
			@ModelAttribute("forecast") ForecastDto form, 
			@PathVariable Long propertyId,
			BindingResult errors, 
			ModelMap modelMap
	) {
		if (!errors.hasErrors()) {
			Property property = propertyService.find(propertyId);
			List<Forecast> formForcasts = form.getForecasts();
			List<Forecast> dbForcasts = forecastService.findAll(property);
			mapChangedValues(formForcasts, dbForcasts);
			forecastService.save(dbForcasts);
			return list(property.getId(), modelMap);
	    } else {
	    	modelMap.addAttribute("errors", errors);
	    	return "forecast/list";
	    }
	}

	private void mapChangedValues(List<Forecast> formForcasts, List<Forecast> dbForcasts) {
		for(int i = 0; i < formForcasts.size(); i++)
			dbForcasts.get(i).setSpecialCost(formForcasts.get(i).getSpecialCost());
	}
}
