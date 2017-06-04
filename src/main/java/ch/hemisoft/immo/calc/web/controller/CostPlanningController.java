package ch.hemisoft.immo.calc.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.hemisoft.immo.calc.business.service.CostPlanningService;
import ch.hemisoft.immo.calc.business.service.PropertyService;
import ch.hemisoft.immo.calc.web.dto.ForecastDto;
import ch.hemisoft.immo.domain.CostPlanning;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("planning")
@RequiredArgsConstructor
public class CostPlanningController {
	@NonNull final CostPlanningService costPlanningService;
	@NonNull final PropertyService propertyService;
	
	@GetMapping("/edit")
	public String edit(ModelMap modelMap) {
		List<Property> properties = propertyService.findAll();
		modelMap.addAttribute("properties", properties);
		return "planning/edit";
	}	
	
	@GetMapping("/edit/{propertyId}")
	public String edit(@PathVariable Long propertyId, ModelMap modelMap) {
		Property property = propertyService.find(propertyId);
		List<CostPlanning> plannings = costPlanningService.findAll(property);
		modelMap.addAttribute("planning", new CostPlanning());
		modelMap.addAttribute("plannings", plannings);
		modelMap.addAttribute("properties", propertyService.findAll());
		modelMap.addAttribute("property", property);
		return "planning/edit";
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
			return edit(property.getId(), modelMap);
	    } else {
	    	modelMap.addAttribute("errors", errors);
	    	return "planning/edit";
	    }
	}
}
