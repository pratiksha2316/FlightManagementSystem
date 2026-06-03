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
    void duplicateResourceExceptionShouldKeepMessageAndBeUncheckedApplicationException() {
        DuplicateResourceException exception = new DuplicateResourceException("flightNumber already exists");

        assertEquals("flightNumber already exists", exception.getMessage());
        assertInstanceOf(FlightManagementException.class, exception);
        assertInstanceOf(RuntimeException.class, exception);
    }

    @Test
    void internalApplicationExceptionShouldKeepMessageAndCause() {
        Exception cause = new Exception("repository failure");

        InternalApplicationException exception = new InternalApplicationException("Unexpected application error", cause);

        assertEquals("Unexpected application error", exception.getMessage());
        assertSame(cause, exception.getCause());
        assertInstanceOf(FlightManagementException.class, exception);
        assertInstanceOf(RuntimeException.class, exception);
    }
}
