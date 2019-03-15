package ch.hemisoft.immo.calc.web.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import ch.hemisoft.commons.exception.EmailExistsException;
import ch.hemisoft.commons.exception.UserNameExistsException;
import ch.hemisoft.immo.calc.business.service.UserService;
import ch.hemisoft.immo.calc.web.dto.UserDto;
import ch.hemisoft.immo.domain.User;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private static final String PAGE_SECURITY_REGISTRATION = "security/registration";
    private static final String REDIRECT_LOGIN_REGISTRATION = "redirect:/login?registration";
    
    private final UserService service;
    
    @GetMapping("/registration")
    public String newRegistration() {
        return PAGE_SECURITY_REGISTRATION;
    }

    @PostMapping("/registration")
    public ModelAndView edit(@ModelAttribute("user") @Valid UserDto user, BindingResult errors) {
        if (!errors.hasErrors()) {
            try {
                ModelAndView mv = new ModelAndView(REDIRECT_LOGIN_REGISTRATION);
                User persistedUser = createUserAccount(user, errors);
                mv.addObject("user", createPopulated(persistedUser));
                return mv;
            } catch (EmailExistsException e) {
                ModelAndView mv = new ModelAndView(PAGE_SECURITY_REGISTRATION);
                mv.addObject("user", user);
                mv.addObject("errors", errors);
                errors.rejectValue("email", "user.email.exist");
                return mv;
            } catch (UserNameExistsException e) {
                ModelAndView mv = new ModelAndView(PAGE_SECURITY_REGISTRATION);
                mv.addObject("user", user);
                mv.addObject("errors", errors);
                errors.rejectValue("userName", "user.userName.exist");
                return mv;
            } 
        } else {
            ModelAndView mv = new ModelAndView(PAGE_SECURITY_REGISTRATION);
            mv.addObject("user", user);
            mv.addObject("errors", errors);
            return mv;
        }
    }
    
    @ModelAttribute("user") 
    public UserDto user() {
        return new UserDto();
    }
    
    private User createUserAccount(UserDto user, BindingResult result) {
        return service.save(createPopulated(user));
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
