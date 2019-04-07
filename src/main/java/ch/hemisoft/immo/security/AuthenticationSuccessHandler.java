package ch.hemisoft.immo.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.github.mkopylec.recaptcha.security.login.LoginFailuresClearingHandler;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthenticationSuccessHandler extends LoginFailuresClearingHandler {
    private static final String FORWARD_URL = "/property/list";
    
    public AuthenticationSuccessHandler(LoginFailuresManager failuresManager) {
        super(failuresManager);
        setDefaultTargetUrl(FORWARD_URL);
    }
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) 
            throws IOException, ServletException {
        log.info("Authentication success: " + authentication.getName());
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
