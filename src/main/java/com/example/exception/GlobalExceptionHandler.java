package com.example.exception;

import com.example.enums.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorDetails> handleInvalidArgsExceptions(MethodArgumentNotValidException ex) {

        List<String> errorMessages = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errorMessages.add(error.getDefaultMessage()));

        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(String.join(". ", errorMessages))
                .details(ErrorCode.BAD_REQUEST.getDescription())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorDetails> handleOtherExceptions(Exception ex) {

        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ex.getMessage())
                .details(ErrorCode.INTERNAL_SERVER_ERROR.getDescription())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}