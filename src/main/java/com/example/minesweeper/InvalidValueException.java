package com.example.minesweeper;

public class InvalidValueException extends RuntimeException {
    public InvalidValueException(String errorMessage) {
        super(errorMessage);
    }
}
