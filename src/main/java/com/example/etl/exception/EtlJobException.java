package com.example.etl.exception;

public class EtlJobException extends RuntimeException {
    public EtlJobException(String message) {
        super(message);
    }

    public EtlJobException(String message, Throwable cause) {
        super(message, cause);
    }
}
