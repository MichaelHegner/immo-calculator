package ch.hemisoft.immo.calc.web.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import ch.hemisoft.immo.domain.CostPlanning;
import ch.hemisoft.immo.validation.FutureLocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class CostPlanningDto {
	private Long id;
	
	@NotNull @NonNull
	private Long propertyId;
	
	@NonNull @NotNull
	@DateTimeFormat(iso =  DateTimeFormat.ISO.DATE)
	@FutureLocalDate
	private LocalDate date;
	
	@NonNull @NotNull
	private BigDecimal amount;
	
	@NonNull @NotNull
	private String type;
	
	@NonNull @NotNull
	@Size(min = 2)
	private String description;
	
	public CostPlanningDto(CostPlanning costPlanning) {
		this.id = costPlanning.getId();
		this.propertyId = costPlanning.getProperty().getId();
		this.date = costPlanning.getDate().getDate();
		this.amount = costPlanning.getAmount();
		this.type = costPlanning.getType().name();
		this.description = costPlanning.getDescription();
	}

}