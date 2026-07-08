package com.student.demo.exception;

public class DuplicateEmailException extends RuntimeException{
    public DuplicateEmailException(String email){
        super("Email '"+ email +"' already exists.");
    }
}
