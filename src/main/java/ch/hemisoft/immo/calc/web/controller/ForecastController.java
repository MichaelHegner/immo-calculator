package ch.hemisoft.immo.calc.web.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ch.hemisoft.immo.calc.business.service.ForecastService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("forecast")
@RequiredArgsConstructor
public class ForecastController {
	@NonNull final ForecastService forecastService;

	@GetMapping("/list")
	public String edit(Principal principal, ModelMap modelMap) {
		modelMap.addAttribute("forecast", forecastService.findAll(principal));
		return "forecast/list";
	}
}
