package ch.hemisoft.immo.calc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	
	@GetMapping(value={"", "login"})
	public String list(ModelMap modelMap) {
		return "/login/login";
	}
}
