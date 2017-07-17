package ch.hemisoft.immo.domain;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
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
			joinColumns 		= @JoinColumn(name = "CREDIT_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PROPERTY_CREDIT_OPTIONS_TO_CREDIT"), unique = true),
			inverseJoinColumns 	= @JoinColumn(name="PROPERTY_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PROPERTY_CREDIT_OPTIONS_TO_PROPERTY")), 
			foreignKey 			= @ForeignKey(name="FK_PROPERTY_CREDIT_OPTIONS_TO_CREDIT"),
			inverseForeignKey 	= @ForeignKey(name="FK_PROPERTY_CREDIT_OPTIONS_TO_PROPERTY")
	)
	@NotNull @NonNull 
	Property 	property;
}