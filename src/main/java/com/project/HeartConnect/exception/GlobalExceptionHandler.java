package com.project.HeartConnect.exception;

import com.project.HeartConnect.utils.response.ErrorResponse;
import com.project.HeartConnect.utils.response.GenericResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

  @ExceptionHandler(HeartConnectException.class)
  public ResponseEntity<ErrorResponse> heartConnectException(
      final HeartConnectException exception) {
    log.error("global exception: ", exception);
    return new ResponseEntity<>(exception.getErrorResponse(), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<GenericResponse<String>> handleException(final Exception exception) {
    // Log the exception
    log.error("Unhandled exception occurred", exception);

    // Return a 500 Internal Server Error response with the stack trace
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(GenericResponse.error("Internal Server Error. Please contact support."));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<GenericResponse<Map<String, String>>> methodArgumentNotValidException(
      final MethodArgumentNotValidException exception) {
    log.error("Method argument not valid exception: ", exception);
    final List<FieldError> errors = exception.getBindingResult().getFieldErrors();
    final Map<String, String> errorFields = new HashMap<>();

    errors.forEach(objectError -> {
      errorFields.put(objectError.getField(), objectError.getDefaultMessage());
    });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GenericResponse.error(errorFields));
  }
}
