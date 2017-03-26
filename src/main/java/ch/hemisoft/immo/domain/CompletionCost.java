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
		if(null == renovation || null == reconstruction)
			return null;
		return renovation.doubleValue() + reconstruction.doubleValue();
	}
}
