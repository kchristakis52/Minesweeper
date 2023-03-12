package com.example.minesweeper;

public class InvalidDescriptionException extends Throwable {
    public InvalidDescriptionException(String errorMessage) {
        super(errorMessage);
    }
}
