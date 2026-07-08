package com.student.demo.exception;

public class InvalidStudentAgeException extends RuntimeException {
    public InvalidStudentAgeException(int age) {
        super("The age should be between 16 and 100");
    }
}
