package ch.hemisoft.immo.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NonNull;

@Embeddable
@Data
public class CustomDate {
	@NonNull @NotNull
	private Integer year;
	
	@NonNull @NotNull
	private Integer month;
	
	@NonNull @NotNull
	private Integer day;
	
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
