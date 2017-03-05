package ch.hemisoft.immo.calc.web.controller;

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

	@GetMapping(value={"", "list"})
	public String list(ModelMap modelMap) {
		modelMap.addAttribute("properties", service.findAll());
		return "/property/list";
	}
	
	@GetMapping("edit")
	public String edit(ModelMap modelMap) {
		modelMap.addAttribute("property", new Property());
		return "property/edit";
	}

	@GetMapping("edit/{propertyId}")
	public String edit(@PathVariable Long propertyId, ModelMap modelMap) {
		modelMap.addAttribute("property", service.find(propertyId));
		return "property/edit";
	}
	
	@PostMapping("save")
	public String save (
			@RequestParam(value="propertyId", required=false) Long id, 
			@ModelAttribute("property") @Valid Property property, 
			BindingResult errors, 
			ModelMap modelMap
	) {
		if (!errors.hasErrors()) {
	        final Property savedProperty = service.save(property);
			modelMap.addAttribute("property", savedProperty);
			return edit(savedProperty.getId(), modelMap);
	    } else {
	    	modelMap.addAttribute("errors", errors);
	    	return "property/edit";
	    }
	}
	
	@ModelAttribute("activeTab")
	public String activeTab() {
		return MvcKey.TAB_PROPERTY;
	}
}
