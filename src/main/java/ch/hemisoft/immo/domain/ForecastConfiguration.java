package ch.hemisoft.immo.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(uniqueConstraints={
        @UniqueConstraint(columnNames = "countryCode", name="UK_FORECAST_CONFIGURATION_ON_COUNTRY_CODE")
}) 
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@Data
public class ForecastConfiguration {
    @Id @GeneratedValue(strategy = IDENTITY)          Long       id;
    @NotNull @Column(unique = true, nullable = false) String     countryCode; // TODO: CHECK VALID CODE
    @NotNull @Column(nullable = false)                Double     taxQuote;
    @NotNull @Column(nullable = false)                Double     runningCostIndex;
    @NotNull @Column(nullable = false)                Double     deprecation;
    @NotNull @Column(nullable = false)                Double     rentalIncrease;
    @NotNull @Column(nullable = false)                Integer    rentalIncreaseFrequence;
}
