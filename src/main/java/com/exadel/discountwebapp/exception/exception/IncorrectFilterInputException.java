package com.exadel.discountwebapp.exception.exception;

import lombok.Getter;

@Getter
public class IncorrectFilterInputException extends ClientInputBaseException {

    private static final String MESSAGE_PATTERN = "Could not find in class %s field %s";

    public IncorrectFilterInputException(String className, String fieldName, Object value) {
        super(className, fieldName, value, MESSAGE_PATTERN);
    }
}
