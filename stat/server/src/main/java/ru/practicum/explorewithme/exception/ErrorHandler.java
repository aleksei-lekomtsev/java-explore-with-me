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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadInputDataException(final BadInputDataException e) {
        log.warn("BadInputDataException: {} HTTP-code: {}", e.getMessage(), HttpStatus.BAD_REQUEST);
        return new ErrorResponse(e.getMessage());
    }
}
