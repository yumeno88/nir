package ru.yumeno.nir.exception_handler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class AdditionFailedException extends RuntimeException {
    public AdditionFailedException(String message) {
        super(message);
    }
}