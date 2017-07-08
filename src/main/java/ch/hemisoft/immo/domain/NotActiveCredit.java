package ch.hemisoft.immo.domain;

import javax.persistence.Entity;
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
	@NotNull @NonNull 
	Property 	property;
}