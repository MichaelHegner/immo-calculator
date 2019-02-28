/**
 *
 */
package ch.hemisoft.immo.aspect.security;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import ch.hemisoft.immo.aspect.ExecutionKey;
import ch.hemisoft.immo.domain.PasswordProtectable;
import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class BusinessServicePasswordSecurity {
	private final PasswordEncoder passwordEncoder;

	@Before(ExecutionKey.USER_SERVICE_EXECUTION + " && args(protectable)")
	public void secureMethod(PasswordProtectable protectable) throws Throwable {
	    String encodedPassword = passwordEncoder.encode(protectable.getPassword());
		protectable.setPassword(encodedPassword);
	}
	
}
