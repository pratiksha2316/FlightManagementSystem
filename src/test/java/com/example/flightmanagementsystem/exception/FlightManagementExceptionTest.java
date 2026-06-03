package com.example.flightmanagementsystem.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

class FlightManagementExceptionTest {

    @Test
    void validationExceptionShouldKeepMessageAndBeUncheckedApplicationException() {
        ValidationException exception = new ValidationException("flightNumber is required");

        assertEquals("flightNumber is required", exception.getMessage());
        assertInstanceOf(FlightManagementException.class, exception);
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void flightManagementExceptionShouldKeepMessageForNonValidationErrors() {
        FlightManagementException exception = new FlightManagementException("flightNumber already exists");

        assertEquals("flightNumber already exists", exception.getMessage());
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void flightManagementExceptionShouldKeepMessageAndCause() {
        Exception cause = new Exception("repository failure");

        FlightManagementException exception = new FlightManagementException("Unexpected application error", cause);

        assertEquals("Unexpected application error", exception.getMessage());
        assertSame(cause, exception.getCause());
        assertInstanceOf(RuntimeException.class, exception);
    }
}
