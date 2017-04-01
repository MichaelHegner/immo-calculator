package ch.hemisoft.immo.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString(callSuper=true, of={"property"})
@EqualsAndHashCode(callSuper=true, of={"property"})
@NoArgsConstructor
@RequiredArgsConstructor
public class NotActiveCredit extends Credit {
	@ManyToOne
	@JoinTable(
			name 				= "PROPERTY_CREDIT_OPTIONS",
			inverseJoinColumns 	= @JoinColumn(name="PROPERTY_ID", nullable = false), 
			joinColumns 		= @JoinColumn(name = "CREDIT_ID", nullable = false, unique = true)
	)
	@NotNull @NonNull 
	Property 	property;
}