package ch.hemisoft.immo.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureLocalDate.FutureValidator.class)
@Documented
public @interface FutureLocalDate {

	String message() default "{javax.validation.constraints.Future.message}";
	
	Class<?>[] groups() default {};
	
	Class<? extends Payload>[] payload() default{};
	
	public class FutureValidator implements ConstraintValidator<FutureLocalDate, LocalDate> {

		@Override
		public void initialize(FutureLocalDate past) {
			// NOTHING TO DO
		}

		@Override
		public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
			return localDate == null || localDate.isAfter(LocalDate.now());
		}
	}
}
