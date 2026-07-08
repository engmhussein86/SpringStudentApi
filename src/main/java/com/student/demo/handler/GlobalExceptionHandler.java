package com.student.demo.handler;

import com.student.demo.exception.DuplicateEmailException;
import com.student.demo.exception.InvalidStudentAgeException;
import com.student.demo.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex){
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(),HttpStatus.NOT_FOUND.value(), LocalDateTime.now())
                , HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Object> handleDuplicateEmail(DuplicateEmailException ex){
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(),HttpStatus.CONFLICT.value(), LocalDateTime.now())
                , HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidStudentAgeException.class)
    public ResponseEntity<Object> handleInvalidAge(InvalidStudentAgeException ex){
        return new ResponseEntity<>(
                new ErrorResponse(ex.getMessage(),HttpStatus.BAD_REQUEST.value(), LocalDateTime.now())
                , HttpStatus.BAD_REQUEST);
    }
}
