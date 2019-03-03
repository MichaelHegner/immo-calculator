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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import ch.hemisoft.immo.calc.business.service.PropertyService;
import ch.hemisoft.immo.calc.web.dto.SessionProperty;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("property")
@SessionAttributes("selectedProperty")
@RequiredArgsConstructor
public class PropertyController {
	@NonNull PropertyService service;

	@GetMapping("/list")
	public String list() {
		return "property/list";
	}
	
	@GetMapping("/new")
	public ModelAndView newProperty() {
	    ModelAndView mv = new ModelAndView("property/edit");
	    mv.addObject("property", new Property());
	    mv.addObject("selectedProperty", new SessionProperty());
		return mv;
	}	
	
	@GetMapping("/edit")
	public ModelAndView edit(@ModelAttribute("selectedProperty") SessionProperty selectedProperty) {
		Long selectedPropertyId = selectedProperty.getId();
		if(null != selectedPropertyId) { // TO GET THE CORRECT URL WITH ID
			return new ModelAndView("redirect:/property/edit/" + selectedPropertyId);
		} else {
		    ModelAndView mv = new ModelAndView("property/edit");
		    mv.addObject("selectedProperty", selectedProperty);
    		return mv;
		}
	}

	@GetMapping("/edit/{propertyId}")
	public ModelAndView edit(@PathVariable Long propertyId) {
	    ModelAndView mv = new ModelAndView("property/edit");
	    mv.addObject("property", service.find(propertyId));
	    mv.addObject("selectedProperty", new SessionProperty(propertyId));
		return mv;
	}
	
	@ModelAttribute("properties")
	public List<Property> properties() {
		return service.findAll();
	}
	
	@ModelAttribute("countryCodes") 
	public List<String> countryCodes() {
		return Arrays.asList("DE", "AT");
	}

	@ModelAttribute("selectedProperty") 
	public SessionProperty sessionProperty() {
		return new SessionProperty();
	}
	
	@ModelAttribute("property") 
	public Property property() {
		return new Property();
	}
	
	@PostMapping("/edit")
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
			
			Long savedPropertyId = savedProperty.getId();
			modelMap.addAttribute("selectedProperty", new SessionProperty(savedPropertyId));
			modelMap.addAttribute("property", savedProperty);
			return "redirect:/property/edit/" + savedPropertyId;
		} else {
			modelMap.addAttribute("property", formProperty);
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
		dbProperty.setYearOfConstruction(formProperty.getYearOfConstruction());
	}
}
