package ch.hemisoft.immo.security;

public class ReCaptchaInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ReCaptchaInvalidException(String message) {
        super(message);
    }
}
