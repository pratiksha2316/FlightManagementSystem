package com.example.flightmanagementsystem.service.impl;

import com.example.flightmanagementsystem.exception.ErrorCode;
import com.example.flightmanagementsystem.entity.Flight;
import com.example.flightmanagementsystem.exception.FlightManagementException;
import com.example.flightmanagementsystem.exception.ValidationException;
import com.example.flightmanagementsystem.model.CreateFlightRequest;
import com.example.flightmanagementsystem.model.FlightResponse;
import com.example.flightmanagementsystem.repository.FlightRepository;
import com.example.flightmanagementsystem.service.FlightService;
import org.springframework.stereotype.Service;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;

    public FlightServiceImpl(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    @Override
    public FlightResponse createFlight(CreateFlightRequest request) {
        validateCreateFlightRequest(request);

        Flight flight = new Flight(request.flightNumber().trim(), request.totalSeats());
        boolean saved = flightRepository.saveIfAbsent(flight);
        if (!saved) {
            throw new FlightManagementException(ErrorCode.FLIGHT_NUMBER_ALREADY_EXISTS);
        }

        return toResponse(flight);
    }

    private void validateCreateFlightRequest(CreateFlightRequest request) {
        if (request == null) {
            throw new ValidationException(ErrorCode.REQUEST_REQUIRED);
        }
        if (request.flightNumber() == null || request.flightNumber().isBlank()) {
            throw new ValidationException(ErrorCode.FLIGHT_NUMBER_REQUIRED);
        }
        if (request.totalSeats() == null) {
            throw new ValidationException(ErrorCode.TOTAL_SEATS_REQUIRED);
        }
        if (request.totalSeats() <= 0) {
            throw new ValidationException(ErrorCode.TOTAL_SEATS_MUST_BE_POSITIVE);
        }
    }

    private FlightResponse toResponse(Flight flight) {
        return new FlightResponse(flight.flightNumber(), flight.totalSeats());
    }
}
