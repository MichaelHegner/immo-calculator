package ch.hemisoft.immo.calc.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.hemisoft.immo.calc.business.service.PropertyService;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("property")
@RequiredArgsConstructor
public class PropertyListController {
	@NonNull PropertyService service;

	@GetMapping(value={"", "list"})
	public String list(ModelMap modelMap) {
		return "/property/list";
	}

	@GetMapping(value={"list/{propertyId}"})
	public String list(@PathVariable long propertyId, ModelMap modelMap) {
		modelMap.addAttribute("property",service.find(propertyId));
		return "/property/list";
	}
	
	@ModelAttribute("properties")
	public List<Property> allProperties() {
		return service.findAll();
	}
}
