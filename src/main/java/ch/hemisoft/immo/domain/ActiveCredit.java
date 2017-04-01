package ch.hemisoft.immo.domain;

import static javax.persistence.FetchType.LAZY;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
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
public class ActiveCredit extends Credit {
	@NotNull @NonNull
	@OneToOne(mappedBy="selectedCredit", fetch=LAZY)	Property 	property;
}