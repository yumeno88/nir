package ru.yumeno.nir.exception_handler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class DeletionFailedException extends RuntimeException {
    public DeletionFailedException(String message) {
        super(message);
    }
}
