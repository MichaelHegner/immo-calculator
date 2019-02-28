package ch.hemisoft.commons.exception;

import ch.hemisoft.immo.domain.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String message = "There is an account with that username: ";
    
    public EmailExistsException(User user) {
        this(user.getUserName());
    }
    
    public EmailExistsException(String userName) {
        super(message + userName);
    }
}
