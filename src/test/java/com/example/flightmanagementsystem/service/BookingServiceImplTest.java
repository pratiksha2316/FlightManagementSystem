package com.example.flightmanagementsystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.flightmanagementsystem.entity.Booking;
import com.example.flightmanagementsystem.entity.Flight;
import com.example.flightmanagementsystem.entity.FlightInstance;
import com.example.flightmanagementsystem.exception.ErrorCode;
import com.example.flightmanagementsystem.exception.FlightManagementException;
import com.example.flightmanagementsystem.exception.ValidationException;
import com.example.flightmanagementsystem.model.BookingResponse;
import com.example.flightmanagementsystem.model.CreateBookingRequest;
import com.example.flightmanagementsystem.repository.BookingRepository;
import com.example.flightmanagementsystem.repository.FlightInstanceRepository;
import com.example.flightmanagementsystem.repository.FlightRepository;
import com.example.flightmanagementsystem.service.impl.BookingServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private FlightInstanceRepository flightInstanceRepository;

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private BookingRepository bookingRepository;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingServiceImpl(flightInstanceRepository, flightRepository, bookingRepository);
    }

    @Test
    void createBookingShouldSaveBookingAndReturnFullResponse() {
        FlightInstance flightInstance = FlightInstance.create("AI101", "2026-06-04 10:30", 3);
        when(flightInstanceRepository.findByFlightInstanceId("AI101_2026-06-04 10:30"))
                .thenReturn(Optional.of(flightInstance));
        when(flightRepository.findByFlightNumber("AI101")).thenReturn(Optional.of(new Flight("AI101", 180)));
        when(bookingRepository.trySaveBooking(any(Booking.class), anyInt())).thenReturn(true);

        BookingResponse response = bookingService.createBooking(
                new CreateBookingRequest(" AI101_2026-06-04 10:30 ", 2, List.of(" Alice ", " Bob "))
        );

        ArgumentCaptor<Booking> bookingCaptor = ArgumentCaptor.forClass(Booking.class);
        verify(bookingRepository).trySaveBooking(bookingCaptor.capture(), anyInt());
        Booking savedBooking = bookingCaptor.getValue();
        assertFalse(savedBooking.bookingId().isBlank());
        assertEquals("AI101_2026-06-04 10:30", savedBooking.flightInstanceId());
        assertEquals(2, savedBooking.numberOfSeats());
        assertEquals(List.of("Alice", "Bob"), savedBooking.passengerNames());
        assertEquals(savedBooking.bookingId(), response.bookingId());
        assertEquals("AI101_2026-06-04 10:30", response.flightInstanceId());
        assertEquals(2, response.numberOfSeats());
        assertEquals(List.of("Alice", "Bob"), response.passengerNames());
    }

    @Test
    void createBookingShouldRejectNullRequest() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(null)
        );

        assertEquals(ErrorCode.REQUEST_REQUIRED, exception.getErrorCode());
        verifyNoRepositoryCalls();
    }

    @Test
    void createBookingShouldRejectBlankFlightInstanceId() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(new CreateBookingRequest(" ", 2, List.of("Alice", "Bob")))
        );

        assertEquals(ErrorCode.FLIGHT_INSTANCE_ID_REQUIRED, exception.getErrorCode());
        verifyNoRepositoryCalls();
    }

    @Test
    void createBookingShouldRejectNullNumberOfSeats() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(new CreateBookingRequest("AI101_2026-06-04 10:30", null, List.of("Alice")))
        );

        assertEquals(ErrorCode.NUMBER_OF_SEATS_REQUIRED, exception.getErrorCode());
        verifyNoRepositoryCalls();
    }

    @Test
    void createBookingShouldRejectZeroNumberOfSeats() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(new CreateBookingRequest("AI101_2026-06-04 10:30", 0, List.of("Alice")))
        );

        assertEquals(ErrorCode.NUMBER_OF_SEATS_MUST_BE_POSITIVE, exception.getErrorCode());
        verifyNoRepositoryCalls();
    }

    @Test
    void createBookingShouldRejectNegativeNumberOfSeats() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(new CreateBookingRequest("AI101_2026-06-04 10:30", -1, List.of("Alice")))
        );

        assertEquals(ErrorCode.NUMBER_OF_SEATS_MUST_BE_POSITIVE, exception.getErrorCode());
        verifyNoRepositoryCalls();
    }

    @Test
    void createBookingShouldRejectNullPassengerNames() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(new CreateBookingRequest("AI101_2026-06-04 10:30", 1, null))
        );

        assertEquals(ErrorCode.PASSENGER_NAMES_REQUIRED, exception.getErrorCode());
        verifyNoRepositoryCalls();
    }

    @Test
    void createBookingShouldRejectEmptyPassengerNames() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(new CreateBookingRequest("AI101_2026-06-04 10:30", 1, List.of()))
        );

        assertEquals(ErrorCode.PASSENGER_NAMES_REQUIRED, exception.getErrorCode());
        verifyNoRepositoryCalls();
    }

    @Test
    void createBookingShouldRejectBlankPassengerName() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(new CreateBookingRequest("AI101_2026-06-04 10:30", 2, List.of("Alice", " ")))
        );

        assertEquals(ErrorCode.PASSENGER_NAME_REQUIRED, exception.getErrorCode());
        verifyNoRepositoryCalls();
    }

    @Test
    void createBookingShouldRejectPassengerCountMismatch() {
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> bookingService.createBooking(new CreateBookingRequest("AI101_2026-06-04 10:30", 2, List.of("Alice")))
        );

        assertEquals(ErrorCode.PASSENGER_COUNT_MISMATCH, exception.getErrorCode());
        verifyNoRepositoryCalls();
    }

    @Test
    void createBookingShouldRejectMissingFlightInstance() {
        when(flightInstanceRepository.findByFlightInstanceId("AI101_2026-06-04 10:30")).thenReturn(Optional.empty());

        FlightManagementException exception = assertThrows(
                FlightManagementException.class,
                () -> bookingService.createBooking(
                        new CreateBookingRequest("AI101_2026-06-04 10:30", 1, List.of("Alice"))
                )
        );

        assertEquals(ErrorCode.FLIGHT_INSTANCE_NOT_FOUND, exception.getErrorCode());
        verify(flightRepository, never()).findByFlightNumber(any());
        verify(bookingRepository, never()).trySaveBooking(any(), anyInt());
    }

    @Test
    void createBookingShouldRejectMissingBaseFlight() {
        FlightInstance flightInstance = FlightInstance.create("AI101", "2026-06-04 10:30", 3);
        when(flightInstanceRepository.findByFlightInstanceId("AI101_2026-06-04 10:30"))
                .thenReturn(Optional.of(flightInstance));
        when(flightRepository.findByFlightNumber("AI101")).thenReturn(Optional.empty());

        FlightManagementException exception = assertThrows(
                FlightManagementException.class,
                () -> bookingService.createBooking(
                        new CreateBookingRequest("AI101_2026-06-04 10:30", 1, List.of("Alice"))
                )
        );

        assertEquals(ErrorCode.FLIGHT_NUMBER_NOT_FOUND, exception.getErrorCode());
        verify(bookingRepository, never()).trySaveBooking(any(), anyInt());
    }

    @Test
    void createBookingShouldRejectWhenSeatsAreNotAvailable() {
        FlightInstance flightInstance = FlightInstance.create("AI101", "2026-06-04 10:30", 3);
        when(flightInstanceRepository.findByFlightInstanceId("AI101_2026-06-04 10:30"))
                .thenReturn(Optional.of(flightInstance));
        when(flightRepository.findByFlightNumber("AI101")).thenReturn(Optional.of(new Flight("AI101", 1)));
        when(bookingRepository.trySaveBooking(any(Booking.class), anyInt())).thenReturn(false);

        FlightManagementException exception = assertThrows(
                FlightManagementException.class,
                () -> bookingService.createBooking(
                        new CreateBookingRequest("AI101_2026-06-04 10:30", 2, List.of("Alice", "Bob"))
                )
        );

        assertEquals(ErrorCode.SEATS_NOT_AVAILABLE, exception.getErrorCode());
    }

    private void verifyNoRepositoryCalls() {
        verify(flightInstanceRepository, never()).findByFlightInstanceId(any());
        verify(flightRepository, never()).findByFlightNumber(any());
        verify(bookingRepository, never()).trySaveBooking(any(), anyInt());
    }
}
