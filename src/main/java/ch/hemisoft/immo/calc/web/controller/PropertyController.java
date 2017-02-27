package ch.hemisoft.immo.calc.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

	// TODO: Make ID more Restful url
	@GetMapping(value={"", "list"})
	public String list(
			@RequestParam(value="propertyId", required=false) Long id, 
			ModelMap modelMap
	) {
		modelMap.addAttribute("property", null==id ? new Property() : service.find(id));
		return "/property/list";
	}
	
	@PostMapping("list")
	public String save (
			@RequestParam(value="propertyId", required=false) Long id, 
			@ModelAttribute("property") @Valid Property property, 
			BindingResult errors, 
			ModelMap model
	) {
		if (!errors.hasErrors()) {
	        final Property savedProperty = service.save(property);
			model.addAttribute("property", savedProperty);
	        model.addAttribute("properties", allProperties());
	    	return "redirect:/property/list?propertyId=" + savedProperty.getId() + "&displayEdit=true";
	    } else {
	    	model.addAttribute("errors", errors);
	    	return "redirect:/property/list?displayEdit=true";
	    }
	}
	
	@ModelAttribute("properties")
	public List<Property> allProperties() {
		return service.findAll();
	}
}
