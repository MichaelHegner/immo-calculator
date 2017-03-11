package ch.hemisoft.immo.calc.web.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.hemisoft.immo.calc.business.service.PropertyService;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("property")
@RequiredArgsConstructor
public class PropertyController {
	@NonNull PropertyService service;

	@GetMapping("/list")
	public String list(Principal principal, ModelMap modelMap) {
		modelMap.addAttribute("properties", service.findAll(principal));
		return "property/list";
	}
	
	@GetMapping("/edit")
	public String edit(Principal principal, ModelMap modelMap) {
		modelMap.addAttribute("property", new Property());
		return "property/edit";
	}

	@GetMapping("/edit/{propertyId}")
	public String edit(@PathVariable Long propertyId, Principal principal, ModelMap modelMap) {
		modelMap.addAttribute("property", service.find(principal, propertyId));
		return "property/edit";
	}
	
	@PostMapping("/save")
	public String save (
			@RequestParam(value="propertyId", required=false) Long id,
			@ModelAttribute("property") @Valid Property property, 
			BindingResult errors, 
			Principal principal, 
			ModelMap modelMap
	) {
		if (!errors.hasErrors()) {
	        final Property savedProperty = service.save(principal, property);
			modelMap.addAttribute("property", savedProperty);
			return edit(savedProperty.getId(), principal, modelMap);
	    } else {
	    	modelMap.addAttribute("errors", errors);
	    	return "property/edit";
	    }
	}
}
