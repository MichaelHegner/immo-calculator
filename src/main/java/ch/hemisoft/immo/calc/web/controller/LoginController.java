package ch.hemisoft.immo.calc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {
	
	@GetMapping({"/", "/login"})
	public String login() {
		return "security/login";
	}
	
	@PostMapping("/logout")
	public ModelAndView logout() {
		return new ModelAndView("redirect:/login?logout");
	}
}
