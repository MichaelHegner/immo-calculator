package ch.hemisoft.immo.calc.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ch.hemisoft.commons.exception.EmailExistsException;
import ch.hemisoft.commons.exception.UserNameExistsException;
import ch.hemisoft.immo.calc.business.service.UserService;
import ch.hemisoft.immo.calc.web.dto.UserDto;
import ch.hemisoft.immo.domain.User;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService service;
    
    @GetMapping("/registration")
    public String newRegistration(ModelMap modelMap) {
        return "security/registration";
    }

    @PostMapping("/registration")
    public String edit(@ModelAttribute("user") @Valid UserDto user, BindingResult errors, ModelMap modelMap) {
        if (!errors.hasErrors()) {
            try {
                User registered = createUserAccount(user, errors);
                modelMap.addAttribute("user", createPopulated(registered));
                return "redirect:/login?registration";
            } 
            catch (EmailExistsException e) {
                modelMap.addAttribute("user", user);
                modelMap.addAttribute("errors", errors);
                errors.rejectValue("email", "user.email.exist");
                return "security/registration";
            } 
            catch (UserNameExistsException e) {
                modelMap.addAttribute("user", user);
                modelMap.addAttribute("errors", errors);
                errors.rejectValue("userName", "user.userName.exist");
                return "security/registration";
            } 
        } else {
            modelMap.addAttribute("user", user);
            modelMap.addAttribute("errors", errors);
            return "security/registration";
        }
    }
    
    @ModelAttribute("user") 
    public UserDto user() {
        return new UserDto();
    }
    
    private User createUserAccount(UserDto user, BindingResult result) {
        try {
            return service.save(createPopulated(user));
        } catch (EmailExistsException e) {
            return null;
        }    
    }
    
    private User createPopulated(UserDto dto) {
        return new User(dto.getUserName(), dto.getPassword(), dto.getEmail(), true); // TODO: Create user deactivated, when there is an admin page to active users
    }
    
    private UserDto createPopulated(User user) {
        UserDto dto = new UserDto();
        dto.setEmail(user.getEmail());
        dto.setUserName(user.getUserName());
        return dto;
    }
}
