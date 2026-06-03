package com.example.flightmanagementsystem.exception;

public class FlightManagementException extends RuntimeException {

    private final ErrorCode errorCode;

    public FlightManagementException(ErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }

    public FlightManagementException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.message(), cause);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
