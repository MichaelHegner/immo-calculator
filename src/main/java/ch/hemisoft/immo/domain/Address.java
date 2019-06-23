package ch.hemisoft.immo.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import groovy.transform.EqualsAndHashCode;
import lombok.Data;

@EqualsAndHashCode
@Embeddable
@Data
public class Address {
    @NotNull @Size(min=5, max=50)          @Column(nullable = false)    private String street;
    @NotNull @Size(min=1, max=5)           @Column(nullable = false)    private String streetNumber;
    @NotNull @Pattern(regexp = "\\d{4,5}") @Column(nullable = false)    private String zip;
    @NotNull @Size(min=2, max=50)          @Column(nullable = false)    private String city;
    @NotNull @Size(min=2, max=5)           @Column(nullable = false)    private String countryCode;
    
    @Override	
    public String toString() {
        return street + " " + streetNumber + ", " + zip + " " + city;
    }
}    
