package ch.hemisoft.immo.domain;

import java.math.BigDecimal;

import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Embeddable
public class RunningCost {
    @NotNull @Min(0)    BigDecimal administrationEachApartment;
    @NotNull @Min(0)    BigDecimal maintenanceEachQm;
}
