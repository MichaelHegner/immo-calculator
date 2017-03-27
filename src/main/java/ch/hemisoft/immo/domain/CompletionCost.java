package ch.hemisoft.immo.domain;

import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;

import lombok.Data;

@Data
@Embeddable
public class CompletionCost {
	@Min(0)		BigDecimal renovation;
	@Min(0)		BigDecimal reconstruction;
	
	public Double getTotalCompletionCost() {
		double renovation = this.renovation == null ? 0.0 : this.renovation.doubleValue();
		double reconstruction = this.reconstruction == null ? 0.0 : this.reconstruction.doubleValue();
		return renovation + reconstruction;
	}
}
