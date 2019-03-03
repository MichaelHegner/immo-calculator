package ch.hemisoft.immo.calc.web.controller;

import static org.springframework.beans.BeanUtils.copyProperties;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import ch.hemisoft.immo.calc.business.service.CostPlanningService;
import ch.hemisoft.immo.calc.business.service.PropertyService;
import ch.hemisoft.immo.calc.web.dto.CostPlanningDto;
import ch.hemisoft.immo.calc.web.dto.SessionProperty;
import ch.hemisoft.immo.domain.CostPlanning;
import ch.hemisoft.immo.domain.CostType;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("planning")
@SessionAttributes("selectedProperty")
@RequiredArgsConstructor
public class CostPlanningController {
	@NonNull final CostPlanningService costPlanningService;
	@NonNull final PropertyService propertyService;
	
	@GetMapping("/list")
	public ModelAndView list() {
	    List<Property> properties = propertyService.findAll();

	    ModelAndView mv = new ModelAndView("planning/edit");
		mv.addObject("property", new Property());
		mv.addObject("selectedProperty", new SessionProperty());
		mv.addObject("properties", properties);
		mv.addObject("plannings", costPlanningService.findAll(properties));
		return mv;
	}	
	
	@GetMapping("/new")
	public ModelAndView newPlanning(@ModelAttribute("selectedProperty") SessionProperty selectedProperty) {
		Long selectedPropertyId = selectedProperty.getId();
		if(null != selectedPropertyId) {
			List<Property> properties = propertyService.findAll();
			
			ModelAndView mv = new ModelAndView("planning/edit");
			mv.addObject("properties", properties);
			mv.addObject("plannings", costPlanningService.findAll(properties)); 
			mv.addObject("selectedProperty", selectedProperty);
			mv.addObject("property", propertyService.find(selectedPropertyId));
			mv.addObject("planning", createNewPlanning(selectedPropertyId));
			return mv;
		} else {
			throw new IllegalStateException("Selecting new cost planning without selected property not allowed.");
		}
	}	
	
	@GetMapping("/edit")
	public ModelAndView edit(@ModelAttribute("selectedProperty") SessionProperty selectedProperty) {
		if(null == selectedProperty.getId()) {
			List<Property> properties = propertyService.findAll();
			
			ModelAndView mv = new ModelAndView("planning/edit");
			mv.addObject("properties", properties);
			mv.addObject("selectedProperty", selectedProperty);
			mv.addObject("plannings", costPlanningService.findAll(properties)); 
			return mv;
		} else {
			return new ModelAndView("redirect:/planning/edit/" + selectedProperty.getId());
		}
	}	
	
	@GetMapping("/edit/{propertyId}")
	public ModelAndView edit(@PathVariable Long propertyId) {
		Property daoProperty = propertyService.find(propertyId);
		
		ModelAndView mv = new ModelAndView("planning/edit");
		mv.addObject("plannings", costPlanningService.findAll(daoProperty)); 
		mv.addObject("properties", propertyService.findAll());
		mv.addObject("property", daoProperty);
		mv.addObject("selectedProperty", new SessionProperty(propertyId));
		return mv;
	}	
	
	@GetMapping("/edit/{propertyId}/planning/{planningId}")
	public ModelAndView edit(@PathVariable Long propertyId, @PathVariable Long planningId) {
		Property daoProperty = propertyService.find(propertyId);
		
		ModelAndView mv = new ModelAndView("planning/edit");
		mv.addObject("planning", getPopulated(costPlanningService.find(planningId)));
		mv.addObject("plannings", costPlanningService.findAll(daoProperty)); 
		mv.addObject("properties", propertyService.findAll());
		mv.addObject("property", daoProperty);
		mv.addObject("selectedProperty", new SessionProperty(propertyId));
		return mv;
	}

	@PostMapping("/edit/{propertyId}")
	public ModelAndView save (
			@PathVariable Long propertyId,
			@ModelAttribute("planning") @Valid CostPlanningDto formCostPlanningDto, 
			BindingResult errors
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
			
			return new ModelAndView("redirect:/planning/edit/" + propertyId);
	    } else {
	        Property daoProperty = propertyService.find(propertyId);
	        
	        ModelAndView mv = new ModelAndView("planning/edit");
	        mv.addObject("plannings", costPlanningService.findAll(daoProperty)); 
	        mv.addObject("properties", propertyService.findAll());
	        mv.addObject("property", daoProperty);
	        mv.addObject("selectedProperty", new SessionProperty(propertyId));
	        mv.addObject("planning", formCostPlanningDto);
	        mv.addObject("errors", errors);
	    	return mv;
	    }
	}
	
	@ModelAttribute("selectedProperty")
	public SessionProperty sessionProperty() {
		return new SessionProperty();
	}

	//

	private CostPlanningDto createNewPlanning(Long propertyId) {
		CostPlanningDto form = new CostPlanningDto();
		if(null != propertyId) form.setPropertyId(propertyId);
		return form;
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
