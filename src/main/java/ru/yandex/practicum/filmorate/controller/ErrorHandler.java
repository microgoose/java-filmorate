package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

@RestControllerAdvice(basePackages = "ru.yandex.practicum.filmorate.controller")
public class ErrorHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        return new ResponseEntity<>(
            new ErrorResponse("Ошибка валидации", ex.getMessage()),
            HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInternalError(IllegalArgumentException ex) {
        return new ResponseEntity<>(
                new ErrorResponse("Неккоректные параметры", ex.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity<>(
            new ErrorResponse("Не найденно", ex.getMessage()),
            HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleInternalError(Exception ex) {
        return new ResponseEntity<>(
            new ErrorResponse("Внутренняя ошибка", ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
