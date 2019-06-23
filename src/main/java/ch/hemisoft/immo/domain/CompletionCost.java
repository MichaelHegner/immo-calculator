package ch.hemisoft.immo.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Embeddable
public class CompletionCost {
    @NotNull @Min(0) @Column(nullable = false) BigDecimal renovation;
    @NotNull @Min(0) @Column(nullable = false) BigDecimal reconstruction;
    
    public Double getTotalCompletionCost() {
        double renovation       = this.renovation     == null ? 0.0 : this.renovation.doubleValue();
        double reconstruction   = this.reconstruction == null ? 0.0 : this.reconstruction.doubleValue();
        return renovation + reconstruction;
    }
}
