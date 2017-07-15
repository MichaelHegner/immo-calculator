package ch.hemisoft.immo.calc.web.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import ch.hemisoft.immo.calc.business.service.ForecastService;
import ch.hemisoft.immo.calc.business.service.PropertyService;
import ch.hemisoft.immo.calc.web.dto.ForecastDto;
import ch.hemisoft.immo.calc.web.dto.SessionProperty;
import ch.hemisoft.immo.domain.Forecast;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("forecast")
@SessionAttributes("selectedProperty")
@RequiredArgsConstructor
public class ForecastController {
	@NonNull final ForecastService forecastService;
	@NonNull final PropertyService propertyService;
	
	@GetMapping("/list")
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("property", new Property());
		modelMap.addAttribute("selectedProperty", new SessionProperty());
		modelMap.addAttribute("properties", propertyService.findAll());
		modelMap.addAttribute("forecast", new ForecastDto(forecastService.findAll(propertyService.findAllConcrete())));
		return "forecast/list";
	}	
	
	@GetMapping("/view")
	public String view(@ModelAttribute("selectedProperty") SessionProperty selectedProperty, ModelMap modelMap) {
		if(null == selectedProperty.getId()) {
			modelMap.addAttribute("selectedProperty", selectedProperty);
			modelMap.addAttribute("properties", propertyService.findAll());
			modelMap.addAttribute("forecast", new ForecastDto(forecastService.findAll(propertyService.findAllConcrete())));
			return "forecast/list";
		} else {
			return "redirect:/forecast/view/" + selectedProperty.getId();
		}
	}	
	
	@GetMapping("/view/{propertyId}")
	public String view(@PathVariable Long propertyId, ModelMap modelMap) {
		Property property = propertyService.find(propertyId);
		List<Property> properties = Arrays.asList(property);
		List<Forecast> forecasts = forecastService.findAll(properties);
		modelMap.addAttribute("forecast", new ForecastDto(forecasts));
		modelMap.addAttribute("properties", propertyService.findAll());
		modelMap.addAttribute("property", property);
		modelMap.addAttribute("selectedProperty", new SessionProperty(propertyId));
		return "forecast/list";
	}
}
