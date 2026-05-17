package com.fintrack.processor.exception;

public class FileParseException extends Exception {
    public FileParseException() {}
    public FileParseException(String message) {
        super(message);
    }
}