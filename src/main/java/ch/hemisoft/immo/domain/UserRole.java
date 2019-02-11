package ch.hemisoft.immo.domain;

import static lombok.AccessLevel.PACKAGE;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@ToString(of="name")
@NoArgsConstructor(access=PACKAGE)
@RequiredArgsConstructor
@Data
public class UserRole {
	@Id @GeneratedValue	private Long 	id;
	@NonNull @NotNull	private String 	name;
}
