package ch.hemisoft.immo.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@ConfigurationProperties(prefix = "recaptcha.validation")
@Data
public class CaptchaSettings {
    private String websiteKey;
    private String secretKey;
}
