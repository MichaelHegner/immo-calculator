package ch.hemisoft.immo.calc.web.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
	public String list() {
		return "/property/list";
	}
	
	@PostMapping("save")
	public String save(@ModelAttribute("property") Property property, BindingResult errors, ModelMap model) {
		if (errors.hasErrors()) {
	        return list();
	    }
	    service.save(property);
	    model.clear();
	    return "redirect:" + list();
	}
	
	@ModelAttribute("property")
	public Property newProperty() {
		return new Property();
	}
	
	@ModelAttribute("properties")
	public List<Property> allProperties() {
		return service.findAll();
	}
}
