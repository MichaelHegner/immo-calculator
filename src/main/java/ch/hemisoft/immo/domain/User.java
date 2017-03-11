package ch.hemisoft.immo.domain;

import static javax.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PROTECTED;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@Entity
@Table(name="users")
@NoArgsConstructor(access=PROTECTED)
@RequiredArgsConstructor
public class User {
	@Id @GeneratedValue	private Long 	id;
	@NonNull @NotNull	private String 	userName;
	@NonNull @NotNull	private String 	password;
	@NonNull @NotNull	private String 	email;
	@NonNull @NotNull	private Boolean enabled;

	@ManyToMany(fetch = EAGER)
	private Collection<UserRole> userRoles = new ArrayList<>();
	
	public User(User user) {
		id = user.id;
		userName = user.userName;
		password = user.password;
		email = user.email;
		enabled = user.enabled;
	}
}
