package ch.hemisoft.immo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ch.hemisoft.immo.calc.backend.repository.UserRepository;
import ch.hemisoft.immo.domain.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SecurityUserServiceImpl implements UserDetailsService {
	@NonNull UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		User user = userRepository.findByUserName(userName);
		if(null == user) {
			throw new UsernameNotFoundException("No user present with username: " + userName);
		} else {
			return new SecurityUser(user);
		}
	}
}
