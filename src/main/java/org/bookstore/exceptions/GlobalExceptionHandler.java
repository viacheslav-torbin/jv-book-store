package org.bookstore.exceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("time", LocalDateTime.now());
        List<String> error = ex.getBindingResult().getAllErrors()
                .stream()
                .map(this::getErrorMessage)
                .collect(Collectors.toList());
        body.put("errors", error);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    private ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errors", ex.getMessage());
        HttpStatusCode status = HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(body, status);
    }

    @ExceptionHandler(value = {DataProcessingException.class})
    private ResponseEntity<Object> handleDataProcessingException(EntityNotFoundException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("errors", ex.getMessage());
        HttpStatusCode status = HttpStatus.I_AM_A_TEAPOT;
        return new ResponseEntity<>(body, status);
    }

    private String getErrorMessage(ObjectError objectError) {
        if (objectError instanceof FieldError) {
            String field = ((FieldError) objectError).getField();
            String message = objectError.getDefaultMessage();
            return field + " " + message;
        }
        return objectError.getDefaultMessage();
    }
}
