package com.example.exception;

public class StudentNotFoundException extends RuntimeException {

    private static final String MESSAGE = "Student with id: %s is not found!";

    public StudentNotFoundException(Long id) {
        super(String.format(MESSAGE, id));
    }
}