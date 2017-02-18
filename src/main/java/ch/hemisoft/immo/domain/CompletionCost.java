package ch.hemisoft.immo.domain;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
@Embeddable
public class CompletionCost {
	double renovation;
	double reconstruction;
	
	public double getTotalCompletionCost() {
		return renovation + reconstruction;
	}
}
