package com.example.flightmanagementsystem.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class FlightManagementExceptionTest {

    @Test
    void validationExceptionShouldKeepMessageAndBeUncheckedApplicationException() {
        ValidationException exception = new ValidationException(ErrorCode.FLIGHT_NUMBER_REQUIRED);

        assertEquals("flightNumber is required", exception.getMessage());
        assertEquals(ErrorCode.FLIGHT_NUMBER_REQUIRED, exception.getErrorCode());
        assertInstanceOf(FlightManagementException.class, exception);
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void flightManagementExceptionShouldKeepErrorCodeAndMessageForNonValidationErrors() {
        FlightManagementException exception = new FlightManagementException(ErrorCode.FLIGHT_NUMBER_ALREADY_EXISTS);

        assertEquals("flightNumber already exists", exception.getMessage());
        assertEquals(ErrorCode.FLIGHT_NUMBER_ALREADY_EXISTS, exception.getErrorCode());
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void flightManagementExceptionShouldKeepMessageAndCause() {
        Exception cause = new Exception("repository failure");

        FlightManagementException exception = new FlightManagementException(ErrorCode.INTERNAL_ERROR, cause);

        assertEquals("Unexpected application error", exception.getMessage());
        assertEquals(ErrorCode.INTERNAL_ERROR, exception.getErrorCode());
        assertSame(cause, exception.getCause());
        assertInstanceOf(RuntimeException.class, exception);
    }
}
