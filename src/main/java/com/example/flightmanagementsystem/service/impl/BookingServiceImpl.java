package com.example.flightmanagementsystem.service.impl;

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
import com.example.flightmanagementsystem.service.BookingService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    private final FlightInstanceRepository flightInstanceRepository;
    private final FlightRepository flightRepository;
    private final BookingRepository bookingRepository;

    public BookingServiceImpl(
            FlightInstanceRepository flightInstanceRepository,
            FlightRepository flightRepository,
            BookingRepository bookingRepository
    ) {
        this.flightInstanceRepository = flightInstanceRepository;
        this.flightRepository = flightRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public BookingResponse createBooking(CreateBookingRequest request) {
        validateCreateBookingRequest(request);

        String flightInstanceId = request.flightInstanceId().trim();
        FlightInstance flightInstance = flightInstanceRepository.findByFlightInstanceId(flightInstanceId)
                .orElseThrow(() -> new FlightManagementException(ErrorCode.FLIGHT_INSTANCE_NOT_FOUND));
        Flight flight = flightRepository.findByFlightNumber(flightInstance.flightNumber())
                .orElseThrow(() -> new FlightManagementException(ErrorCode.FLIGHT_NUMBER_NOT_FOUND));

        Booking booking = Booking.create(
                flightInstanceId,
                request.numberOfSeats(),
                trimPassengerNames(request.passengerNames())
        );
        boolean saved = bookingRepository.trySaveBooking(booking, flight.totalSeats());
        if (!saved) {
            throw new FlightManagementException(ErrorCode.SEATS_NOT_AVAILABLE);
        }

        return toResponse(booking);
    }

    private void validateCreateBookingRequest(CreateBookingRequest request) {
        if (request == null) {
            throw new ValidationException(ErrorCode.REQUEST_REQUIRED);
        }
        if (request.flightInstanceId() == null || request.flightInstanceId().isBlank()) {
            throw new ValidationException(ErrorCode.FLIGHT_INSTANCE_ID_REQUIRED);
        }
        if (request.numberOfSeats() == null) {
            throw new ValidationException(ErrorCode.NUMBER_OF_SEATS_REQUIRED);
        }
        if (request.numberOfSeats() <= 0) {
            throw new ValidationException(ErrorCode.NUMBER_OF_SEATS_MUST_BE_POSITIVE);
        }
        if (request.passengerNames() == null || request.passengerNames().isEmpty()) {
            throw new ValidationException(ErrorCode.PASSENGER_NAMES_REQUIRED);
        }
        if (request.passengerNames().stream().anyMatch(passengerName -> passengerName == null || passengerName.isBlank())) {
            throw new ValidationException(ErrorCode.PASSENGER_NAME_REQUIRED);
        }
        if (request.numberOfSeats() != request.passengerNames().size()) {
            throw new ValidationException(ErrorCode.PASSENGER_COUNT_MISMATCH);
        }
    }

    private List<String> trimPassengerNames(List<String> passengerNames) {
        return passengerNames.stream()
                .map(String::trim)
                .toList();
    }

    private BookingResponse toResponse(Booking booking) {
        return new BookingResponse(
                booking.bookingId(),
                booking.flightInstanceId(),
                booking.numberOfSeats(),
                booking.passengerNames()
        );
    }
}
