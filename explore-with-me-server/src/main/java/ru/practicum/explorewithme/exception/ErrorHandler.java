package ru.practicum.explorewithme.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explorewithme.model.dto.ApiError;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final MethodArgumentNotValidException e) {
        ApiError error = new ApiError();
        e.getAllErrors()
                .forEach(er -> error.getErrors().add(er.toString()));
        error.setStatus(HttpStatus.BAD_REQUEST.name());
        error.setMessage(e.getMessage());
        error.setReason("Method argument not valid");
        error.setTimestamp(Timestamp.from(Instant.now()));
        return error;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError handleForbiddenException(final ForbiddenException e) {
        ApiError error = new ApiError();
        Arrays.stream(e.getStackTrace())
                .forEach(er -> error.getErrors().add(er.toString()));
        error.setStatus(HttpStatus.BAD_REQUEST.name());
        error.setMessage(e.getMessage());
        error.setReason("Forbidden");
        error.setTimestamp(Timestamp.from(Instant.now()));
        return error;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        ApiError error = new ApiError();
        Arrays.stream(e.getStackTrace())
                .forEach(er -> error.getErrors().add(er.toString()));
        error.setStatus(HttpStatus.NOT_FOUND.name());
        error.setMessage(e.getMessage());
        error.setReason("Not found");
        error.setTimestamp(Timestamp.from(Instant.now()));
        return error;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException e) {
        ApiError error = new ApiError();
        Arrays.stream(e.getStackTrace())
                .forEach(er -> error.getErrors().add(er.toString()));
        error.setStatus(HttpStatus.BAD_REQUEST.name());
        error.setMessage(e.getMessage());
        error.setReason("Bad request");
        error.setTimestamp(Timestamp.from(Instant.now()));
        return error;
    }
}
