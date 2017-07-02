package ch.hemisoft.immo.domain;

import static javax.persistence.FetchType.LAZY;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import ch.hemisoft.immo.utils.BigDecimalUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(uniqueConstraints={
    @UniqueConstraint(columnNames = {"year", "month", "day", "property_id", "type"})
}) 
@Data
@ToString(of={"date", "property", "type"})
@EqualsAndHashCode(of={"date", "property", "type"})
@NoArgsConstructor
@RequiredArgsConstructor
public class CostPlanning implements Ownable{
	@Id @GeneratedValue
	private Long id;
	
	@NotNull @NonNull
	@OneToOne(fetch=LAZY)	
	private Property property;

	@AttributeOverrides({
        @AttributeOverride(name = "year", column = @Column(name = "year")),
        @AttributeOverride(name = "month", column = @Column(name = "month")),
        @AttributeOverride(name = "day", column = @Column(name = "day"))
	})
	@Embedded
	@NonNull @NotNull
	private CustomDate date = new CustomDate();
	
	@NonNull @NotNull
	@Column(precision=10, scale=2)
	private BigDecimal amount 				= BigDecimalUtils.convert(0.0);

	@NonNull @NotNull
	private CostType type;
	
	@NonNull @NotNull
	private String description;
	
	//
	
	public CostPlanning(Property property, CostType type, LocalDate date, String description) {
		this.property = property;
		this.date.setDate(date);
		this.type = type;
		this.description = description;
	}
	
	//
	
	public void setDate(LocalDate date) {
		this.date.setDate(Objects.requireNonNull(date));
	}
	
	@Override
	public User getOwner() {
		return property.getOwner();
	}

	@Override
	public void setOwner(User owner) {
		// DONT SET FROM HERE!
		throw new UnsupportedOperationException();
	}
}


