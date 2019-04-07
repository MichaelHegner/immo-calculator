package ch.hemisoft.immo.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.github.mkopylec.recaptcha.RecaptchaProperties;
import com.github.mkopylec.recaptcha.security.login.LoginFailuresManager;
import com.google.common.util.concurrent.AtomicLongMap;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CaptchaLoginFailuresManager extends LoginFailuresManager {
    public static AtomicLongMap<String> failureCount = AtomicLongMap.create();

    public CaptchaLoginFailuresManager(RecaptchaProperties recaptcha) {
        super(recaptcha);
    }

    @Override
    public void addLoginFailure(HttpServletRequest request) {
        String username = getUsername(request);
        log.warn("Login Failure of user '{}'", username);
        failureCount.getAndIncrement(username);
    }

    @Override
    public int getLoginFailuresCount(HttpServletRequest request) {
        String username = getUsername(request);
        long count = failureCount.get(getUsername(request));
        log.debug("Login Failure Count for user '{}' = '{}'", username, count);
        return (int) count;
    }

    @Override
    public void clearLoginFailures(HttpServletRequest request) {
        String username = getUsername(request);
        log.debug("Clear Login Failure for user '{}'.", username);
        failureCount.remove(username);
    }
}
