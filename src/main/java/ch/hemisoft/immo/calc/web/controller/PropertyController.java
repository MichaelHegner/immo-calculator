package ch.hemisoft.immo.calc.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("property")
public class PropertyController {

	@GetMapping("edit")
	public String edit(){
		return "/property/edit";
	}
}
