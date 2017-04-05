package ch.hemisoft.immo.calc.web.controller;

import static java.lang.Boolean.FALSE;

import java.security.Principal;
import java.util.Collection;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ch.hemisoft.immo.calc.business.service.InvestmentService;
import ch.hemisoft.immo.calc.business.service.PropertyService;
import ch.hemisoft.immo.domain.Credit;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("investment")
@RequiredArgsConstructor
public class InvestmentController {
	@NonNull final PropertyService propertyService;
	@NonNull final InvestmentService investmentService;

	@GetMapping("/edit")
	public String edit(Principal principal, ModelMap modelMap) {
		modelMap.addAttribute("properties", propertyService.findAll(principal));
		modelMap.addAttribute("property", null);
		return "investment/edit";
	}

	@GetMapping("/edit/{propertyId}")
	public String edit(
			@PathVariable Long propertyId, 
			Principal principal, 
			ModelMap modelMap, 
			@RequestParam(value="deactivate", defaultValue="false") Boolean deactivateCredit
	) {
		final Property property;
		if(deactivateCredit) {
			property = investmentService.deactivateCredit(principal, propertyId);
		} else {
			property = propertyService.find(principal, propertyId);
		}
		
		modelMap.addAttribute("properties", propertyService.findAll(principal));
		modelMap.addAttribute("property", property);
		return "investment/edit";
	}

	@PostMapping("/save")
	public String save (
			@ModelAttribute("property") Property formProperty, 
			BindingResult errors, 
			Principal principal, 
			ModelMap modelMap
	) {
		if (!errors.hasErrors()) {
			final Property dbProperty = propertyService.find(principal, formProperty.getId());
			mapChangedValues(formProperty, dbProperty, principal);
	        final Property savedProperty = propertyService.save(principal, dbProperty);
			modelMap.addAttribute("property", savedProperty);
			return edit(savedProperty.getId(), principal, modelMap, FALSE);
	    } else {
	    	modelMap.addAttribute("errors", errors);
	    	return "investment/edit";
	    }
	}
	
	//

	// TODO: Try to Remove Mapping
	private Property mapChangedValues(Property formProperty, Property dbProperty, Principal principal) {
		dbProperty.setNetAssets(formProperty.getNetAssets());
		mapChangedValues(formProperty.getSelectedCredit(), dbProperty.getSelectedCredit());
		mapChangedValues(formProperty.getCreditOptions(), dbProperty.getCreditOptions());
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
		if(null == formCredit && null == dbCredit) return;
		
		dbCredit.setNameOfInstitution(formCredit.getNameOfInstitution());
		dbCredit.setInterestRateNominalInPercent(formCredit.getInterestRateNominalInPercent());
		dbCredit.setNameOfInstitution(formCredit.getNameOfInstitution());
		dbCredit.setRedemptionAtBeginInPercent(formCredit.getRedemptionAtBeginInPercent());
		dbCredit.setSpecialRedemptionEachYearInPercent(formCredit.getSpecialRedemptionEachYearInPercent());
	}
}
