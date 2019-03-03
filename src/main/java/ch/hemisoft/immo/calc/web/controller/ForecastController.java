package ch.hemisoft.immo.calc.web.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import ch.hemisoft.immo.calc.business.service.ForecastService;
import ch.hemisoft.immo.calc.business.service.PropertyService;
import ch.hemisoft.immo.calc.business.service.vo.ForecastVO;
import ch.hemisoft.immo.calc.web.dto.ForecastTableDto;
import ch.hemisoft.immo.calc.web.dto.SessionProperty;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("forecast")
@SessionAttributes("selectedProperty")
@RequiredArgsConstructor
public class ForecastController {
	@NonNull final ForecastService forecastService;
	@NonNull final PropertyService propertyService;
	
	@GetMapping("/list")
	public ModelAndView list() {
	    ModelAndView mv = new ModelAndView("forecast/list");
	    mv.addObject("property", new Property());
	    mv.addObject("selectedProperty", new SessionProperty());
	    mv.addObject("properties", propertyService.findAll());
	    mv.addObject("forecast", new ForecastTableDto(forecastService.findAll(propertyService.findAllConcrete())));
		return mv;
	}	
	
	@GetMapping("/view")
	public ModelAndView view(@ModelAttribute("selectedProperty") SessionProperty selectedProperty) {
		if(null == selectedProperty.getId()) {
		    ModelAndView mv = new ModelAndView("forecast/list");
		    mv.addObject("selectedProperty", selectedProperty);
		    mv.addObject("properties", propertyService.findAll());
		    mv.addObject("forecast", new ForecastTableDto(forecastService.findAll(propertyService.findAllConcrete())));
			return mv;
		} else {
			return new ModelAndView("redirect:/forecast/view/" + selectedProperty.getId());
		}
	}	
	
	@GetMapping("/view/{propertyId}")
	public ModelAndView view(@PathVariable Long propertyId) {
		Property property = propertyService.find(propertyId);
		List<Property> properties = Arrays.asList(property);
		List<ForecastVO> forecasts = forecastService.findAll(properties);
		
		ModelAndView mv = new ModelAndView("forecast/list");
		mv.addObject("forecast", new ForecastTableDto(forecasts));
		mv.addObject("properties", propertyService.findAll());
		mv.addObject("property", property);
		mv.addObject("selectedProperty", new SessionProperty(propertyId));
		return mv;
	}
}
