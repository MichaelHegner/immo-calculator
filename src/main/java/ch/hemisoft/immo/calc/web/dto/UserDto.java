package ch.hemisoft.immo.calc.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ch.hemisoft.immo.aspect.annotation.PasswordMatches;
import ch.hemisoft.immo.aspect.annotation.ValidEmail;
import groovy.transform.ToString;
import lombok.Data;

@Data
@PasswordMatches
@ToString(excludes = {"password", "matchingPassword"})
public class UserDto {
    @NotNull
    @NotEmpty
    private String userName;
     
    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    @NotEmpty
    private String matchingPassword;
     
    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;
}