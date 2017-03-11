/**
 *
 */
package ch.hemisoft.immo.aspect.security;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import ch.hemisoft.immo.aspect.ExecutionKey;
import ch.hemisoft.immo.domain.Ownable;
import ch.hemisoft.immo.security.SecurityUser;

@Aspect
@Component
public class BusinessServiceSecurity {
	private static final String ALL = ExecutionKey.BUSINESS_SERVICE_EXECUTION;
	
	@Autowired private UserDetailsService userDetailService;

	@Around(ALL + " && args(principal, ownable)")
	public Object secureMethod(final ProceedingJoinPoint point, Principal principal, Ownable ownable) throws Throwable {
		populatePrincipalToOwnable(principal, ownable);
		return filterOwnedByPrincipleObjects(principal, point.proceed());
	}

	@Around(ALL + " && args(principal, ownableId)")
	public Object secureMethod(final ProceedingJoinPoint point, Principal principal, Long ownableId) throws Throwable {
		return filterOwnedByPrincipleObjects(principal, point.proceed());
	}
	
	@Around(ALL + " && args(principal)")
	public Object secureMethod(final ProceedingJoinPoint point, Principal principal) throws Throwable {
		return filterOwnedByPrincipleObjects(principal, point.proceed());
	}

	//
	
	private void populatePrincipalToOwnable(Principal principal, Ownable ownable) {
		String username = principal.getName();
		SecurityUser userDetails = (SecurityUser) userDetailService.loadUserByUsername(username);
		ownable.setOwner(userDetails);
	}
	
	//
	
	@SuppressWarnings("unchecked")
	private Object filterOwnedByPrincipleObjects(Principal principal, Object object) {
		if(object instanceof List<?>) {
			List<Ownable> list = (List<Ownable>) object;
			return filterObjectList(principal, list);
		} else if (object instanceof Ownable) {
			Ownable ownable = (Ownable) object;
			return filterOwnable(principal, ownable);
		} else {
			return object;
		}
	}

	private List<Ownable> filterObjectList(Principal principal, List<Ownable> list) {
		return list.stream().filter(ownable -> isPrincipalOwner(principal, ownable)).collect(Collectors.toList());
	}

	private Ownable filterOwnable(Principal principal, Ownable ownable) {
		return isPrincipalOwner(principal, ownable) ? ownable : null;
	}

	//
	
	private Boolean isPrincipalOwner(Principal principal, Ownable ownable) {
		return StringUtils.equalsIgnoreCase(ownable.getOwner().getUserName(), principal.getName());
	}
}
