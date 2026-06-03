package com.example.flightmanagementsystem.exception;

public class FlightManagementException extends RuntimeException {

    public FlightManagementException(String message) {
        super(message);
    }

    public FlightManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}
