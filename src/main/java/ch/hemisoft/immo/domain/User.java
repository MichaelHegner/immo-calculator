package ch.hemisoft.immo.domain;

import static java.util.Objects.requireNonNull;
import static javax.persistence.FetchType.EAGER;
import static lombok.AccessLevel.PROTECTED;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
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

	@JoinTable(
			foreignKey = @ForeignKey(name = "FK_USER_ROLE_TO_USER"),
			inverseForeignKey = @ForeignKey(name = "FK_USER_TO_USER_ROLE")
	)
	@ManyToMany(fetch = EAGER)
	private Collection<UserRole> userRoles = new ArrayList<>();
	
	public User(User user) {
		id = requireNonNull(user.getId());
		userName = requireNonNull(user.getUserName());
		password = requireNonNull(user.getPassword());
		email = requireNonNull(user.getEmail());
		enabled = requireNonNull(user.getEnabled());
	}
}
