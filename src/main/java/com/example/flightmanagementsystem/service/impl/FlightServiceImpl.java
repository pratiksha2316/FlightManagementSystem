package com.example.flightmanagementsystem.service.impl;

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
            throw new FlightManagementException("flightNumber already exists");
        }

        return toResponse(flight);
    }

    private void validateCreateFlightRequest(CreateFlightRequest request) {
        if (request == null) {
            throw new ValidationException("request is required");
        }
        if (request.flightNumber() == null || request.flightNumber().isBlank()) {
            throw new ValidationException("flightNumber is required");
        }
        if (request.totalSeats() == null) {
            throw new ValidationException("totalSeats is required");
        }
        if (request.totalSeats() <= 0) {
            throw new ValidationException("totalSeats must be positive");
        }
    }

    private FlightResponse toResponse(Flight flight) {
        return new FlightResponse(flight.flightNumber(), flight.totalSeats());
    }
}
