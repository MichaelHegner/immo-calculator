package ch.hemisoft.immo.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@PropertySource("classpath:config/captcha.properties")
@ConfigurationProperties(prefix = "google.recaptcha.key")
@Data
public class CaptchaSettings {
    private String site;
    private String secret;
}
