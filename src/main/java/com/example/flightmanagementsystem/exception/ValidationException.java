package com.example.flightmanagementsystem.exception;

public class ValidationException extends FlightManagementException {

    public ValidationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
