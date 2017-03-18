package ch.hemisoft.immo.calc.web.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.hemisoft.immo.calc.business.service.PropertyService;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("investment")
@RequiredArgsConstructor
public class InvestmentController {
	@NonNull PropertyService service;

	@GetMapping("/edit")
	public String edit(Principal principal, ModelMap modelMap) {
		modelMap.addAttribute("properties", service.findAll(principal));
		modelMap.addAttribute("property", service.findAll(principal).get(0));
		return "investment/edit";
	}

	@GetMapping("/edit/{propertyId}")
	public String edit(@PathVariable Long propertyId, Principal principal, ModelMap modelMap) {
		modelMap.addAttribute("properties", service.findAll(principal));
		modelMap.addAttribute("property", service.find(principal, propertyId));
		return "investment/edit";
	}

	@PostMapping("/save")
	public String save (
			@ModelAttribute("property") Property property, 
			BindingResult errors, 
			Principal principal, 
			ModelMap modelMap
	) {
		if (!errors.hasErrors()) {
			Property dbProperty = service.find(principal, property.getId());
			dbProperty.setNetAssets(property.getNetAssets());
	        final Property savedProperty = service.save(principal, dbProperty);
			modelMap.addAttribute("property", savedProperty);
			return edit(savedProperty.getId(), principal, modelMap);
	    } else {
	    	modelMap.addAttribute("errors", errors);
	    	return "investment/edit";
	    }
	}
}
