package org.cunha.exception;

import java.time.LocalDateTime;

public class InvalidScheduleDateException extends CustomException {

    public InvalidScheduleDateException(LocalDateTime dateTime) {
        super(String.format("The date %s is invalid! The send date can't be before the current time!", dateTime.toString()));
    }

}
