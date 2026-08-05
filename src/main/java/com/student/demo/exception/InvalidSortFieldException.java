package com.student.demo.exception;

public class InvalidSortFieldException extends RuntimeException {

    public InvalidSortFieldException(String field) {
        super("Cannot sort by field: " + field);
    }
}
