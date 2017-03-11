package ch.hemisoft.immo.security;

import static java.lang.Boolean.TRUE;
import static org.springframework.util.StringUtils.collectionToCommaDelimitedString;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import ch.hemisoft.immo.domain.User;

public class SecurityUser extends User implements UserDetails {
	private static final long serialVersionUID = 1L;

	public SecurityUser(User user) {
		super(user);
	}
	
	SecurityUser() {
		// Needed by Framework
		super();
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		String roles = collectionToCommaDelimitedString(getUserRoles());
		return AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
	}

	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	public String getUsername() {
		return super.getUserName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return TRUE;
	}

	@Override
	public boolean isAccountNonLocked() {
		return TRUE;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return TRUE;
	}

	@Override
	public boolean isEnabled() {
		return super.getEnabled();
	}

}
