package ru.yumeno.nir.exception_handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yumeno.nir.dto.ResourceErrorDTO;
import ru.yumeno.nir.exception_handler.exceptions.DeletionFailedException;
import ru.yumeno.nir.exception_handler.exceptions.ResourceNotFoundException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ResourceErrorDTO> handleException(ResourceNotFoundException e) {
        ResourceErrorDTO resourceErrorDTO = new ResourceErrorDTO(e.getMessage());
        return new ResponseEntity<>(resourceErrorDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ResourceErrorDTO> handleException(DeletionFailedException e) {
        ResourceErrorDTO resourceErrorDTO = new ResourceErrorDTO(e.getMessage());
        return new ResponseEntity<>(resourceErrorDTO, HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler
    public ResponseEntity<ResourceErrorDTO> handleException(Exception e) {
        ResourceErrorDTO resourceErrorDTO = new ResourceErrorDTO(e.getMessage());
        return new ResponseEntity<>(resourceErrorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
