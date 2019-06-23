package ch.hemisoft.immo.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Embeddable
@NoArgsConstructor
@Data
public class CustomDate {
    @NonNull @NotNull @Column(nullable = false) private Integer year;
    @NonNull @NotNull @Column(nullable = false) private Integer month;
    @NonNull @NotNull @Column(nullable = false) private Integer day;
    
    public LocalDate getDate() {
        return LocalDate.of(year, month, day);
    }
    
    public void setDate(LocalDate date) {
        Objects.requireNonNull(date);
        this.year = date.get(ChronoField.YEAR);
        this.month = date.get(ChronoField.MONTH_OF_YEAR);
        this.day = date.get(ChronoField.DAY_OF_MONTH);
    }
}
