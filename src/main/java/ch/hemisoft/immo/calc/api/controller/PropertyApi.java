package ch.hemisoft.immo.calc.api.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.hemisoft.immo.calc.business.service.PropertyService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("property")
@RequiredArgsConstructor
public class PropertyApi {
	@NonNull PropertyService service;

	@DeleteMapping("/delete/{id}")
	public void delete(@PathVariable Long id) {
		service.deleteById(id);
	}
}
