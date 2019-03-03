package ch.hemisoft.immo.calc.web.controller;

import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import ch.hemisoft.immo.calc.business.service.InvestmentService;
import ch.hemisoft.immo.calc.business.service.PropertyService;
import ch.hemisoft.immo.calc.web.dto.SessionProperty;
import ch.hemisoft.immo.domain.Credit;
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
	public ModelAndView newCredit(@ModelAttribute("selectedProperty") SessionProperty selectedProperty) {
		Long selectedPropertyId = selectedProperty.getId();

		ModelAndView mv = new ModelAndView("investment/edit");
		if(null != selectedPropertyId) {
			Property property = propertyService.find(selectedPropertyId);
			if(!property.getCredits().stream().filter(c -> null == c.getId()).findAny().isPresent()) {
				property.addCreditOptions(new Credit(property));
			}
			mv.addObject("selectedProperty", selectedProperty);
			mv.addObject("property", property);
		}
		return mv;
	}	
	
	@GetMapping("/edit")
	public ModelAndView edit(@ModelAttribute("selectedProperty") SessionProperty selectedProperty) {
		if(null == selectedProperty.getId()) {
		    ModelAndView mv = new ModelAndView("investment/edit");
		    mv.addObject("properties", propertyService.findAll());
		    mv.addObject("property", new Property());
		    mv.addObject("selectedProperty", selectedProperty);
			return mv;
		} else {
			return new ModelAndView("redirect:/investment/edit/" + selectedProperty.getId());
		}
	}
	
	@GetMapping("/edit/{propertyId}")
	public ModelAndView edit(@PathVariable Long propertyId) {
	    ModelAndView mv = new ModelAndView("investment/edit");
	    mv.addObject("properties", propertyService.findAll());
	    mv.addObject("property", propertyService.find(propertyId));
	    mv.addObject("selectedProperty", new SessionProperty(propertyId));
		return mv;
	}

	@GetMapping("/edit/{propertyId}/credit/{creditId}/swap")
	public ModelAndView swapCredit(@PathVariable Long propertyId, @PathVariable Long creditId) {
		investmentService.swapCredit(creditId);
		
		ModelAndView mv = new ModelAndView("investment/edit");
		mv.addObject("property", propertyService.find(propertyId));
		mv.addObject("selectedProperty", new SessionProperty(propertyId));
		return mv;
	}
	
	@PostMapping("/edit")
	public ModelAndView save (@ModelAttribute("property") Property formProperty, BindingResult errors) {
		formProperty.getCredits().stream().forEach(c -> validator.validate(c, errors));
		if (!errors.hasErrors()) {
			final Property dbProperty = propertyService.find(formProperty.getId());
			mapChangedValues(formProperty, dbProperty);
	        final Property savedProperty = propertyService.save(dbProperty);
	        
	        ModelAndView mv = new ModelAndView("redirect:/investment/edit/" + savedProperty.getId());
	        mv.addObject("property", savedProperty);
	        mv.addObject("selectedProperty", new SessionProperty(savedProperty.getId()));
			return mv;
	    } else {
	        ModelAndView mv = new ModelAndView("investment/edit");
	        mv.addObject("property", formProperty);
	        mv.addObject("errors", errors);
	    	return mv;
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
		mapChangedValues(formProperty.getCredits(), dbProperty.getCredits());
		formProperty.getCredits().stream().filter(c -> c.getId() == null).forEach(c -> dbProperty.addCreditOptions(c));
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
		copyProperties(formCredit, dbCredit, "property");
	}
}
