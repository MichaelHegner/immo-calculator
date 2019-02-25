/**
 *
 */
package ch.hemisoft.immo.aspect.security;

import java.security.Principal;
import java.util.Iterator;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.thymeleaf.util.StringUtils;

import ch.hemisoft.immo.aspect.ExecutionKey;
import ch.hemisoft.immo.domain.Ownable;
import ch.hemisoft.immo.security.SecurityUser;

@Aspect
@Component
public class BusinessServiceOwnerSecurity {
	private static final String ALL = ExecutionKey.BUSINESS_SERVICE_EXECUTION;
	
	@Autowired private UserDetailsService userDetailService;

	@Around(ALL + " && args(ownable)")
	public Object secureMethod(final ProceedingJoinPoint point, Ownable ownable) throws Throwable {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		handleNotAuthenticatedPrinciple(principal);
		populatePrincipalToOwnable(principal, ownable);
		return filterOwnedByPrincipleObjects(principal, point.proceed());
	}
	
	@Around(ALL)
	public Object secureMethod(final ProceedingJoinPoint point) throws Throwable {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		handleNotAuthenticatedPrinciple(principal);
		return filterOwnedByPrincipleObjects(principal, point.proceed());
	}

	//
	
	private void populatePrincipalToOwnable(Principal principal, Ownable ownable) {
		if(null == ownable.getOwner() || null == ownable.getOwner().getId()) {
			String username = principal.getName();
			SecurityUser userDetails = (SecurityUser) userDetailService.loadUserByUsername(username);
			ownable.setOwner(userDetails);
		} else if (!StringUtils.equals(principal.getName(), ownable.getOwner().getUserName())) {
			throw new AccessDeniedException("The access to the requested data has been denied.");
		}
	}
	
	//
	
	private Object filterOwnedByPrincipleObjects(Principal principal, Object object) {
		if(object instanceof List<?>) {
			List<?> list = (List<?>) object;
			Iterator<?> iterator = list.iterator();
			
			while(iterator.hasNext()) {
				Object element = iterator.next();
				if(element instanceof Ownable) {
					Ownable ownable = (Ownable) element;
					if(!isPrincipalOwner(principal, ownable)) {
						iterator.remove();
					}
				}
			}
			
			return list;
		} else if (object instanceof Ownable) {
			Ownable ownable = (Ownable) object;
			return filterOwnable(principal, ownable);
		} else {
			return object;
		}
	}

	private Ownable filterOwnable(Principal principal, Ownable ownable) {
		if (isPrincipalOwner(principal, ownable)) {
			return ownable;
		}
		throw new AccessDeniedException("The access to the requested data has been denied.");
	}

	//
	
	private Boolean isPrincipalOwner(Principal principal, Ownable ownable) {
		return StringUtils.equalsIgnoreCase(ownable.getOwner().getUserName(), principal.getName());
	}
	
	//
	
	private void handleNotAuthenticatedPrinciple(Principal principal) {
		if(null == principal.getName()) {
			throw new AuthenticationCredentialsNotFoundException("Username is null!");
		}
	}
}
