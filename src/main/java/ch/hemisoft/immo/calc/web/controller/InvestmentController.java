package ch.hemisoft.immo.calc.web.controller;

import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.util.CollectionUtils.isEmpty;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import ch.hemisoft.immo.calc.business.service.InvestmentService;
import ch.hemisoft.immo.calc.business.service.PropertyService;
import ch.hemisoft.immo.calc.web.dto.SessionProperty;
import ch.hemisoft.immo.domain.Credit;
import ch.hemisoft.immo.domain.NotActiveCredit;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("investment")
@SessionAttributes("selectedProperty")
@RequiredArgsConstructor
public class InvestmentController {
	@NonNull final PropertyService propertyService;
	@NonNull final InvestmentService investmentService;
	@NonNull final Validator validator;
	
	@GetMapping("/new")
	public String newProperty(@ModelAttribute("selectedProperty") SessionProperty selectedProperty, ModelMap modelMap) {
		Long selectedPropertyId = selectedProperty.getId();
		if(null != selectedPropertyId) {
			Property property = propertyService.find(selectedPropertyId);
			if(isEmpty(property.getCreditOptions())) {
				property.addCreditOptions(new NotActiveCredit(property));
			}
			modelMap.addAttribute("selectedProperty", selectedProperty);
			modelMap.addAttribute("property", property);
		}
		return "investment/edit";
	}	
	
	@GetMapping("/edit")
	public String edit(@ModelAttribute("selectedProperty") SessionProperty selectedProperty, ModelMap modelMap) {
		if(null == selectedProperty.getId()) {
			modelMap.addAttribute("properties", propertyService.findAll());
			modelMap.addAttribute("property", new Property());
			modelMap.addAttribute("selectedProperty", selectedProperty);
			return "investment/edit";
		} else {
			return "redirect:/investment/edit/" + selectedProperty.getId();
		}
	}

	@GetMapping("/edit/{propertyId}")
	public String edit(
			@PathVariable Long propertyId, 
			ModelMap modelMap, 
			@RequestParam(value = "deactivate", defaultValue = "false") Boolean deactivateCredit,
			@RequestParam(value = "activate", required = false) Long idOfCreditToActivate
	) {
		final Property property;
		if(deactivateCredit) {
			property = investmentService.deactivateCredit(propertyId);
		} else if (null != idOfCreditToActivate) {
			property = investmentService.activateCredit(propertyId, idOfCreditToActivate);
		} else {
			property = propertyService.find(propertyId);
		}
		
		modelMap.addAttribute("property", property);
		modelMap.addAttribute("selectedProperty", new SessionProperty(property.getId()));
		return "investment/edit";
	}

	@PostMapping("/edit")
	public String save (
			@ModelAttribute("property") Property formProperty, 
			BindingResult errors, 
			ModelMap modelMap
	) {
		formProperty.getCreditOptions().stream().forEach(c -> validator.validate(c, errors));
		if (!errors.hasErrors()) {
			final Property dbProperty = propertyService.find(formProperty.getId());
			mapChangedValues(formProperty, dbProperty);
	        final Property savedProperty = propertyService.save(dbProperty);
			modelMap.addAttribute("property", savedProperty);
			modelMap.addAttribute("selectedProperty", new SessionProperty(savedProperty.getId()));
			return "redirect:/investment/edit/" + savedProperty.getId();
	    } else {
	    	modelMap.addAttribute("property", formProperty);
	    	modelMap.addAttribute("errors", errors);
	    	return "investment/edit";
	    }
	}
	
	@ModelAttribute("properties")
	public List<Property> properties() {
		return propertyService.findAll();
	}	
	
	@ModelAttribute("selectedProperty")
	public SessionProperty sessionProperty() {
		return new SessionProperty();
	}
	
	//

	// TODO: Try to Remove Mapping
	private Property mapChangedValues(Property formProperty, Property dbProperty) {
		dbProperty.setNetAssets(formProperty.getNetAssets());
		mapChangedValues(formProperty.getSelectedCredit(), dbProperty.getSelectedCredit());
		mapChangedValues(formProperty.getCreditOptions(), dbProperty.getCreditOptions());
		formProperty.getCreditOptions().stream().filter(c -> c.getId() == null).forEach(c -> dbProperty.addCreditOptions(c));
		return dbProperty;
	}
	
	private void mapChangedValues(Collection<? extends Credit> formCreditOptions, Collection<? extends Credit> dbCreditOptions) {
		for(Credit dbCreditOption : dbCreditOptions) {
			for(Credit formCreditOption : formCreditOptions) {
				if(dbCreditOption.getNameOfInstitution().equals(formCreditOption.getNameOfInstitution())) {
					mapChangedValues(formCreditOption, dbCreditOption);
				}
			}
		}
	}
	
	private void mapChangedValues(Credit formCredit, Credit dbCredit) {
		if(null == formCredit || null == dbCredit) return;
		copyProperties(formCredit, dbCredit);
	}
}
