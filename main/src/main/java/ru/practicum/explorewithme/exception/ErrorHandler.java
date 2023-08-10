package ru.practicum.explorewithme.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictException(final ConflictException e) {
        log.warn("ConflictException: {} HTTP-code: {}", e.getMessage(), HttpStatus.CONFLICT);
        return new ErrorResponse(" A request conflict with the current state of the target resource.");
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleEntityNotFoundException(final EntityNotFoundException e) {
        log.warn("EntityNotFoundException: {} HTTP-code: {}", e.getMessage(), HttpStatus.NOT_FOUND);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadInputDataException(final BadInputDataException e) {
        log.warn("BadInputDataException: {} HTTP-code: {}", e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ErrorResponse(e.getMessage());
    }

}
