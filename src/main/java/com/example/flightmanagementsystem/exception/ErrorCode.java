package com.example.flightmanagementsystem.exception;

public enum ErrorCode {
    REQUEST_REQUIRED(400, "request is required"),
    FLIGHT_NUMBER_REQUIRED(400, "flightNumber is required"),
    TOTAL_SEATS_REQUIRED(400, "totalSeats is required"),
    TOTAL_SEATS_MUST_BE_POSITIVE(400, "totalSeats must be positive"),
    FLIGHT_NUMBER_ALREADY_EXISTS(409, "flightNumber already exists"),
    INITIAL_DEPARTURE_TIME_REQUIRED(400, "initialDepartureTime is required"),
    FLIGHT_HOURS_REQUIRED(400, "flightHours is required"),
    FLIGHT_HOURS_MUST_BE_POSITIVE(400, "flightHours must be positive"),
    FLIGHT_NUMBER_NOT_FOUND(404, "flightNumber does not exist"),
    FLIGHT_INSTANCE_ALREADY_EXISTS(409, "flightInstance already exists"),
    INTERNAL_ERROR(500, "Unexpected application error");

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
