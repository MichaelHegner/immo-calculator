package ch.hemisoft.immo.calc.web.controller;

import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.ArrayList;
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

import ch.hemisoft.immo.calc.business.service.CostPlanningService;
import ch.hemisoft.immo.calc.business.service.PropertyService;
import ch.hemisoft.immo.calc.web.dto.CostPlanningDto;
import ch.hemisoft.immo.domain.CostPlanning;
import ch.hemisoft.immo.domain.CostType;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("planning")
@RequiredArgsConstructor
public class CostPlanningController {
	@NonNull final CostPlanningService costPlanningService;
	@NonNull final PropertyService propertyService;
	
	@GetMapping("/edit")
	public String edit(ModelMap modelMap) {
		List<Property> properties = propertyService.findAll();
		modelMap.addAttribute("properties", properties);
		modelMap.addAttribute("plannings", costPlanningService.findAll(properties)); 
		return "planning/edit";
	}	
	
	@GetMapping("/edit/{propertyId}")
	public String edit(@PathVariable Long propertyId, ModelMap modelMap) {
		Property daoProperty = propertyService.find(propertyId);
		modelMap.addAttribute("plannings", costPlanningService.findAll(daoProperty)); 
		modelMap.addAttribute("properties", propertyService.findAll());
		modelMap.addAttribute("property", daoProperty);
		return "planning/edit";
	}	
	
	@GetMapping("/edit/{propertyId}/planning/{planningId}")
	public String edit(@PathVariable Long propertyId, @PathVariable Long planningId, ModelMap modelMap) {
		Property daoProperty = propertyService.find(propertyId);
		modelMap.addAttribute("planning", getPopulated(costPlanningService.find(planningId)));
		modelMap.addAttribute("plannings", costPlanningService.findAll(daoProperty)); 
		modelMap.addAttribute("properties", propertyService.findAll());
		modelMap.addAttribute("property", daoProperty);
		return "planning/edit";
	}

	@PostMapping("/edit/{propertyId}")
	public String save (
			@ModelAttribute("planning") @Valid CostPlanningDto formCostPlanningDto, 
			BindingResult errors, 
			ModelMap modelMap
	) {
		if (!errors.hasErrors()) {
			Long planningId = formCostPlanningDto.getId();
			CostPlanning formCostPlanning = getPopulated(formCostPlanningDto);
			
			if(null == planningId) {
				costPlanningService.save(formCostPlanning);
			} else {
				CostPlanning dbCostPlanning = costPlanningService.find(planningId);
				mapChangedValue(formCostPlanning, dbCostPlanning);
				costPlanningService.save(dbCostPlanning);
			}
			
			return "redirect:/planning/edit/" + formCostPlanningDto.getPropertyId();
	    } else {
	    	modelMap.addAttribute("errors", errors);
	    	return edit(formCostPlanningDto.getPropertyId(), modelMap);
	    }
	}

	@ModelAttribute("planning")
	public CostPlanningDto newPlanning(@PathVariable(required=false) Long propertyId) {
		CostPlanningDto form = new CostPlanningDto();
		if(null != propertyId) form.setPropertyId(propertyId);
		return form;
	}
	
	//

	// TODO: Try to Remove Mapping
	private List<CostPlanningDto> getPopulated(List<CostPlanning> daoCostPlannings) {
		List<CostPlanningDto> dtoCostPlannings = new ArrayList<>();
		for(int i = 0; i < daoCostPlannings.size(); i++) {
			dtoCostPlannings.add(getPopulated(daoCostPlannings.get(i)));
		}
		return dtoCostPlannings;
	}

	private CostPlanningDto getPopulated(CostPlanning costPlanning) {
		return new CostPlanningDto(costPlanning);
	}
	
	private CostPlanning getPopulated(CostPlanningDto dtoCostPlanning) {
		CostPlanning daoCostPlanning = new CostPlanning();
		populate(dtoCostPlanning, daoCostPlanning);
		return daoCostPlanning;
	}
	
	private void populate(CostPlanningDto dtoCostPlanning, CostPlanning daoCostPlanning) {
		daoCostPlanning.setAmount(dtoCostPlanning.getAmount());
		daoCostPlanning.setDate(dtoCostPlanning.getDate());
		daoCostPlanning.setDescription(dtoCostPlanning.getDescription());
		daoCostPlanning.setId(dtoCostPlanning.getId());
		daoCostPlanning.setProperty(propertyService.find(dtoCostPlanning.getPropertyId()));
		daoCostPlanning.setType(CostType.valueOf(dtoCostPlanning.getType()));
	}
	
	private void mapChangedValue(CostPlanning formCostPlanning, CostPlanning dbCostPlanning) {
		copyProperties(formCostPlanning, dbCostPlanning, "owner");
	}
}
