package com.example.flightmanagementsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.flightmanagementsystem.entity.Flight;
import com.example.flightmanagementsystem.exception.ErrorCode;
import com.example.flightmanagementsystem.exception.FlightManagementException;
import com.example.flightmanagementsystem.exception.ValidationException;
import com.example.flightmanagementsystem.model.CreateFlightRequest;
import com.example.flightmanagementsystem.model.FlightResponse;
import com.example.flightmanagementsystem.repository.FlightRepository;
import com.example.flightmanagementsystem.service.impl.FlightServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FlightServiceImplTest {

    @Mock
    private FlightRepository flightRepository;

    private FlightService flightService;

    @BeforeEach
    void setUp() {
        flightService = new FlightServiceImpl(flightRepository);
    }

    @Test
    void createFlightShouldSaveFlightAndReturnFullResponse() {
        when(flightRepository.saveIfAbsent(any(Flight.class))).thenReturn(true);

        FlightResponse response = flightService.createFlight(new CreateFlightRequest(" AI101 ", 180));

        ArgumentCaptor<Flight> flightCaptor = ArgumentCaptor.forClass(Flight.class);
        verify(flightRepository).saveIfAbsent(flightCaptor.capture());
        Flight savedFlight = flightCaptor.getValue();
        assertEquals("AI101", savedFlight.flightNumber());
        assertEquals(180, savedFlight.totalSeats());
        assertEquals("AI101", response.flightNumber());
        assertEquals(180, response.totalSeats());
    }

    @Test
    void createFlightShouldRejectNullRequest() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> flightService.createFlight(null)
        );

        assertEquals("request is required", exception.getMessage());
        assertEquals(ErrorCode.REQUEST_REQUIRED, exception.getErrorCode());
        verify(flightRepository, never()).saveIfAbsent(any());
    }

    @Test
    void createFlightShouldRejectNullFlightNumber() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> flightService.createFlight(new CreateFlightRequest(null, 180))
        );

        assertEquals("flightNumber is required", exception.getMessage());
        assertEquals(ErrorCode.FLIGHT_NUMBER_REQUIRED, exception.getErrorCode());
        verify(flightRepository, never()).saveIfAbsent(any());
    }

    @Test
    void createFlightShouldRejectBlankFlightNumber() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> flightService.createFlight(new CreateFlightRequest(" ", 180))
        );

        assertEquals("flightNumber is required", exception.getMessage());
        assertEquals(ErrorCode.FLIGHT_NUMBER_REQUIRED, exception.getErrorCode());
        verify(flightRepository, never()).saveIfAbsent(any());
    }

    @Test
    void createFlightShouldRejectNullTotalSeats() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> flightService.createFlight(new CreateFlightRequest("AI101", null))
        );

        assertEquals("totalSeats is required", exception.getMessage());
        assertEquals(ErrorCode.TOTAL_SEATS_REQUIRED, exception.getErrorCode());
        verify(flightRepository, never()).saveIfAbsent(any());
    }

    @Test
    void createFlightShouldRejectZeroTotalSeats() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> flightService.createFlight(new CreateFlightRequest("AI101", 0))
        );

        assertEquals("totalSeats must be positive", exception.getMessage());
        assertEquals(ErrorCode.TOTAL_SEATS_MUST_BE_POSITIVE, exception.getErrorCode());
        verify(flightRepository, never()).saveIfAbsent(any());
    }

    @Test
    void createFlightShouldRejectNegativeTotalSeats() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> flightService.createFlight(new CreateFlightRequest("AI101", -1))
        );

        assertEquals("totalSeats must be positive", exception.getMessage());
        assertEquals(ErrorCode.TOTAL_SEATS_MUST_BE_POSITIVE, exception.getErrorCode());
        verify(flightRepository, never()).saveIfAbsent(any());
    }

    @Test
    void createFlightShouldRejectDuplicateFlightNumber() {
        when(flightRepository.saveIfAbsent(any(Flight.class))).thenReturn(false);

        FlightManagementException exception = assertThrows(
                FlightManagementException.class,
                () -> flightService.createFlight(new CreateFlightRequest("AI101", 180))
        );

        assertEquals("flightNumber already exists", exception.getMessage());
        assertEquals(ErrorCode.FLIGHT_NUMBER_ALREADY_EXISTS, exception.getErrorCode());
    }
}
