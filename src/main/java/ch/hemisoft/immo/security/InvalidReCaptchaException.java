package ch.hemisoft.immo.security;

public class InvalidReCaptchaException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidReCaptchaException(String message) {
        super(message);
    }
}
