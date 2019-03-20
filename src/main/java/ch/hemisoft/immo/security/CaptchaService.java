package ch.hemisoft.immo.security;

import static org.springframework.util.StringUtils.hasLength;

import java.net.URI;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CaptchaService {
    private static final String URL_RECAPTCHA = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s";
    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
    
    private final CaptchaSettings captchaSettings;
    private final ReCaptchaAttemptService reCaptchaAttemptService;
    private final RestOperations restTemplate;
    private final HttpServletRequest request;
 
    public void processResponse(String response) {
        if (reCaptchaAttemptService.isBlocked(getClientIP()))  throw new ReCaptchaInvalidException("Client exceeded maximum number of failed attempts"); 
        if(!responseSanityCheck(response)) throw new InvalidReCaptchaException("Response contains invalid characters");

        GoogleResponse googleResponse = recaptcha(response);
        if(!googleResponse.isSuccess()) {
            if(googleResponse.hasClientError()) {
                reCaptchaAttemptService.reCaptchaFailed(getClientIP());
            }
            throw new ReCaptchaInvalidException("reCaptcha was not successfully validated"); 
        }
        
        reCaptchaAttemptService.reCaptchaSucceeded(getClientIP());
    }

    private GoogleResponse recaptcha(String response) {
        URI verifyUri = URI.create(String.format(URL_RECAPTCHA, getReCaptchaSecret(), response, getClientIP()));
        return restTemplate.getForObject(verifyUri, GoogleResponse.class);
    }
 
    private boolean responseSanityCheck(String response) {
        return hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }
    
    public String getReCaptchaSite() {
        return captchaSettings.getSite();
    }

    public String getReCaptchaSecret() {
        return captchaSettings.getSecret();
    }
    
    private String getClientIP() {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
