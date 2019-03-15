package ch.hemisoft.commons.exception;

import ch.hemisoft.immo.domain.User;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserNameExistsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String message = "There is an account already with that user name: ";
    
    public UserNameExistsException(User user) {
        this(user.getEmail());
    }
    
    public UserNameExistsException(String email) {
        super(message + email);
    }
}
