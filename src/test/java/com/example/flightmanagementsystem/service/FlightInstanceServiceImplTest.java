package com.example.flightmanagementsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.flightmanagementsystem.entity.FlightInstance;
import com.example.flightmanagementsystem.exception.ErrorCode;
import com.example.flightmanagementsystem.exception.FlightManagementException;
import com.example.flightmanagementsystem.exception.ValidationException;
import com.example.flightmanagementsystem.model.CreateFlightInstanceRequest;
import com.example.flightmanagementsystem.model.FlightInstanceResponse;
import com.example.flightmanagementsystem.repository.FlightInstanceRepository;
import com.example.flightmanagementsystem.repository.FlightRepository;
import com.example.flightmanagementsystem.service.impl.FlightInstanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FlightInstanceServiceImplTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private FlightInstanceRepository flightInstanceRepository;

    private FlightInstanceService flightInstanceService;

    @BeforeEach
    void setUp() {
        flightInstanceService = new FlightInstanceServiceImpl(flightRepository, flightInstanceRepository);
    }

    @Test
    void createFlightInstanceShouldSaveFlightInstanceAndReturnFullResponse() {
        when(flightRepository.existsByFlightNumber("AI101")).thenReturn(true);
        when(flightInstanceRepository.saveIfAbsent(any(FlightInstance.class))).thenReturn(true);

        FlightInstanceResponse response = flightInstanceService.createFlightInstance(
                new CreateFlightInstanceRequest(" AI101 ", " 2026-06-04 10:30 ", 3)
        );

        ArgumentCaptor<FlightInstance> flightInstanceCaptor = ArgumentCaptor.forClass(FlightInstance.class);
        verify(flightInstanceRepository).saveIfAbsent(flightInstanceCaptor.capture());
        FlightInstance savedFlightInstance = flightInstanceCaptor.getValue();
        assertEquals("AI101_2026-06-04 10:30", savedFlightInstance.flightInstanceId());
        assertEquals("AI101", savedFlightInstance.flightNumber());
        assertEquals("2026-06-04 10:30", savedFlightInstance.initialDepartureTime());
        assertEquals(3, savedFlightInstance.flightHours());
        assertEquals("AI101_2026-06-04 10:30", response.flightInstanceId());
        assertEquals("AI101", response.flightNumber());
        assertEquals("2026-06-04 10:30", response.initialDepartureTime());
        assertEquals(3, response.flightHours());
    }

    @Test
    void createFlightInstanceShouldRejectNullRequest() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> flightInstanceService.createFlightInstance(null)
        );

        assertEquals(ErrorCode.REQUEST_REQUIRED, exception.getErrorCode());
        verify(flightRepository, never()).existsByFlightNumber(any());
        verify(flightInstanceRepository, never()).saveIfAbsent(any());
    }

    @Test
    void createFlightInstanceShouldRejectBlankFlightNumber() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> flightInstanceService.createFlightInstance(new CreateFlightInstanceRequest(" ", "2026-06-04 10:30", 3))
        );

        assertEquals(ErrorCode.FLIGHT_NUMBER_REQUIRED, exception.getErrorCode());
        verify(flightRepository, never()).existsByFlightNumber(any());
        verify(flightInstanceRepository, never()).saveIfAbsent(any());
    }

    @Test
    void createFlightInstanceShouldRejectNullInitialDepartureTime() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> flightInstanceService.createFlightInstance(new CreateFlightInstanceRequest("AI101", null, 3))
        );

        assertEquals(ErrorCode.INITIAL_DEPARTURE_TIME_REQUIRED, exception.getErrorCode());
        verify(flightRepository, never()).existsByFlightNumber(any());
        verify(flightInstanceRepository, never()).saveIfAbsent(any());
    }

    @Test
    void createFlightInstanceShouldRejectBlankInitialDepartureTime() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> flightInstanceService.createFlightInstance(new CreateFlightInstanceRequest("AI101", " ", 3))
        );

        assertEquals(ErrorCode.INITIAL_DEPARTURE_TIME_REQUIRED, exception.getErrorCode());
        verify(flightRepository, never()).existsByFlightNumber(any());
        verify(flightInstanceRepository, never()).saveIfAbsent(any());
    }

    @Test
    void createFlightInstanceShouldRejectNullFlightHours() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> flightInstanceService.createFlightInstance(new CreateFlightInstanceRequest("AI101", "2026-06-04 10:30", null))
        );

        assertEquals(ErrorCode.FLIGHT_HOURS_REQUIRED, exception.getErrorCode());
        verify(flightRepository, never()).existsByFlightNumber(any());
        verify(flightInstanceRepository, never()).saveIfAbsent(any());
    }

    @Test
    void createFlightInstanceShouldRejectZeroFlightHours() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> flightInstanceService.createFlightInstance(new CreateFlightInstanceRequest("AI101", "2026-06-04 10:30", 0))
        );

        assertEquals(ErrorCode.FLIGHT_HOURS_MUST_BE_POSITIVE, exception.getErrorCode());
        verify(flightRepository, never()).existsByFlightNumber(any());
        verify(flightInstanceRepository, never()).saveIfAbsent(any());
    }

    @Test
    void createFlightInstanceShouldRejectNegativeFlightHours() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> flightInstanceService.createFlightInstance(new CreateFlightInstanceRequest("AI101", "2026-06-04 10:30", -1))
        );

        assertEquals(ErrorCode.FLIGHT_HOURS_MUST_BE_POSITIVE, exception.getErrorCode());
        verify(flightRepository, never()).existsByFlightNumber(any());
        verify(flightInstanceRepository, never()).saveIfAbsent(any());
    }

    @Test
    void createFlightInstanceShouldRejectMissingFlightNumber() {
        when(flightRepository.existsByFlightNumber("AI101")).thenReturn(false);

        FlightManagementException exception = assertThrows(
                FlightManagementException.class,
                () -> flightInstanceService.createFlightInstance(new CreateFlightInstanceRequest("AI101", "2026-06-04 10:30", 3))
        );

        assertEquals(ErrorCode.FLIGHT_NUMBER_NOT_FOUND, exception.getErrorCode());
        verify(flightInstanceRepository, never()).saveIfAbsent(any());
    }

    @Test
    void createFlightInstanceShouldRejectDuplicateFlightInstance() {
        when(flightRepository.existsByFlightNumber("AI101")).thenReturn(true);
        when(flightInstanceRepository.saveIfAbsent(any(FlightInstance.class))).thenReturn(false);

        FlightManagementException exception = assertThrows(
                FlightManagementException.class,
                () -> flightInstanceService.createFlightInstance(new CreateFlightInstanceRequest("AI101", "2026-06-04 10:30", 3))
        );

        assertEquals(ErrorCode.FLIGHT_INSTANCE_ALREADY_EXISTS, exception.getErrorCode());
    }
}
