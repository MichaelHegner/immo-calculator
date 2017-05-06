package ch.hemisoft.immo.calc.web.controller;

import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

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
@RequestMapping("property")
@RequiredArgsConstructor
public class PropertyController {
	@NonNull PropertyService service;

	@GetMapping("/list")
	public String list(ModelMap modelMap) {
		return "property/list";
	}
	
	@GetMapping("/edit")
	public String edit(ModelMap modelMap) {
		modelMap.addAttribute("property", new Property());
		return "property/edit";
	}

	@GetMapping("/edit/{propertyId}")
	public String edit(@PathVariable Long propertyId, ModelMap modelMap) {
		modelMap.addAttribute("property", service.find(propertyId));
		return "property/edit";
	}
	
	@ModelAttribute("properties")
	public List<Property> properties() {
		return service.findAll();
	}
	
	@ModelAttribute("countryCodes") 
	public List<String> countryCodes() {
		return Arrays.asList("DE", "AT");
	}
	
	@PostMapping("/save")
	public String save (
			@ModelAttribute("property") @Valid Property formProperty, 
			BindingResult errors, 
			ModelMap modelMap
	) {
		if (!errors.hasErrors()) {
			Long id = formProperty.getId();
			final Property savedProperty;

			if (null == id) {
				savedProperty = service.save(formProperty);
			} else {
				final Property dbProperty = service.find(id);
				mapChangedValues(formProperty, dbProperty);
				savedProperty = service.save(dbProperty);
			}
			
			modelMap.addAttribute("property", savedProperty);
			return edit(savedProperty.getId(), modelMap);
		} else {
	    	modelMap.addAttribute("errors", errors);
	    	return "property/edit";
	    }
	}
	
	private void mapChangedValues(Property formProperty, Property dbProperty) {
		dbProperty.setAddress(formProperty.getAddress());
		dbProperty.setCompletionCost(formProperty.getCompletionCost());
		dbProperty.setLandAreaInQm(formProperty.getLandAreaInQm());
		dbProperty.setLivingSpaceInQm(formProperty.getLivingSpaceInQm());
		dbProperty.setNetAssets(formProperty.getNetAssets());
		dbProperty.setNoApartments(formProperty.getNoApartments());
		dbProperty.setNoParking(formProperty.getNoParking());
		dbProperty.setPurchaseCost(formProperty.getPurchaseCost());
		dbProperty.setPurchaseDate(formProperty.getPurchaseDate());
		dbProperty.setPurchasePrice(formProperty.getPurchasePrice());
		dbProperty.setRentalNet(formProperty.getRentalNet());
		dbProperty.setRunningCost(formProperty.getRunningCost());
		dbProperty.setType(formProperty.getType());
		dbProperty.setStatus(formProperty.getStatus());
	}
}
