package ar.edu.utn.frc.tup.lc.iv.controllers;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.ErrorApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.Timestamp;
import java.time.ZonedDateTime;

/**
 * Controller for managing Exceptions in Controllers .
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    /**
     * Handles all uncaught exceptions within the application.
     *
     * @param exception The thrown exception.
     * @return ResponseEntity with HTTP status 500 (Internal Server Error) and a body containing error details.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorApi> handleError(Exception exception) {
        ErrorApi error = buildError(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    private ErrorApi buildError(String message, HttpStatus status) {
        return ErrorApi.builder()
                .timestamp(String.valueOf(Timestamp.from(ZonedDateTime.now().toInstant())))
                .error(status.getReasonPhrase())
                .status(status.value())
                .message(message)
                .build();
    }
}
