package ch.hemisoft.commons.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import ch.hemisoft.immo.aspect.annotation.PasswordMatches;
import ch.hemisoft.immo.calc.web.dto.UserDto;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> { 
   
  @Override
  public void initialize(PasswordMatches constraintAnnotation) {}

  @Override
  public boolean isValid(Object obj, ConstraintValidatorContext context){   
      UserDto user = (UserDto) obj;
      return user.getPassword().equals(user.getMatchingPassword());    
  }     
}