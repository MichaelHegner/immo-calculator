package ch.hemisoft.immo.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;

import lombok.Data;

@Data
@Embeddable
public class CompletionCost {
	@Min(0)		double renovation;
	@Min(0)		double reconstruction;
	
	public double getTotalCompletionCost() {
		return renovation + reconstruction;
	}
}
