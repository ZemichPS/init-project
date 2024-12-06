package by.zemich.userms.service.exceptions;

import jakarta.persistence.EntityNotFoundException;

public class UserNotFoundException extends EntityNotFoundException {
    public UserNotFoundException() {
        super("User is nowhere to be found");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
