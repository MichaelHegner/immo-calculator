package ch.hemisoft.immo.calc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.hemisoft.immo.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByUserName(String userName);
}