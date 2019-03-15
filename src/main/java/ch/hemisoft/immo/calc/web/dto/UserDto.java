package ch.hemisoft.immo.calc.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import ch.hemisoft.immo.aspect.annotation.PasswordMatches;
import ch.hemisoft.immo.aspect.annotation.ValidEmail;
import groovy.transform.ToString;
import lombok.Data;

@Data
@PasswordMatches
@ToString(excludes = {UserDto.PASSWORD, UserDto.MATCHING_PASSWORD})
public class UserDto {
    public static final String USER_NAME = "userName";
    public static final String PASSWORD = "password";
    public static final String MATCHING_PASSWORD = "matchingPassword";
    public static final String EMAIL = "email";
    
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