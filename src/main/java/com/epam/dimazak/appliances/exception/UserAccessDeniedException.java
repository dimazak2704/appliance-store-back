package com.epam.dimazak.appliances.exception;

public class UserAccessDeniedException extends RuntimeException {
    public UserAccessDeniedException(String message) {
        super(message);
    }
}