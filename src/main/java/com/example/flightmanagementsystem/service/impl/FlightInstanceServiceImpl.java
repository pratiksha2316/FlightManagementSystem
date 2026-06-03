package com.example.flightmanagementsystem.service.impl;

import com.example.flightmanagementsystem.entity.FlightInstance;
import com.example.flightmanagementsystem.exception.ErrorCode;
import com.example.flightmanagementsystem.exception.FlightManagementException;
import com.example.flightmanagementsystem.exception.ValidationException;
import com.example.flightmanagementsystem.model.CreateFlightInstanceRequest;
import com.example.flightmanagementsystem.model.FlightInstanceResponse;
import com.example.flightmanagementsystem.repository.FlightInstanceRepository;
import com.example.flightmanagementsystem.repository.FlightRepository;
import com.example.flightmanagementsystem.service.FlightInstanceService;
import org.springframework.stereotype.Service;

@Service
public class FlightInstanceServiceImpl implements FlightInstanceService {

    private final FlightRepository flightRepository;
    private final FlightInstanceRepository flightInstanceRepository;

    public FlightInstanceServiceImpl(
            FlightRepository flightRepository,
            FlightInstanceRepository flightInstanceRepository
    ) {
        this.flightRepository = flightRepository;
        this.flightInstanceRepository = flightInstanceRepository;
    }

    @Override
    public FlightInstanceResponse createFlightInstance(CreateFlightInstanceRequest request) {
        validateCreateFlightInstanceRequest(request);

        String flightNumber = request.flightNumber().trim();
        String initialDepartureTime = request.initialDepartureTime().trim();
        if (!flightRepository.existsByFlightNumber(flightNumber)) {
            throw new FlightManagementException(ErrorCode.FLIGHT_NUMBER_NOT_FOUND);
        }

        FlightInstance flightInstance = FlightInstance.create(flightNumber, initialDepartureTime, request.flightHours());
        boolean saved = flightInstanceRepository.saveIfAbsent(flightInstance);
        if (!saved) {
            throw new FlightManagementException(ErrorCode.FLIGHT_INSTANCE_ALREADY_EXISTS);
        }

        return toResponse(flightInstance);
    }

    private void validateCreateFlightInstanceRequest(CreateFlightInstanceRequest request) {
        if (request == null) {
            throw new ValidationException(ErrorCode.REQUEST_REQUIRED);
        }
        if (request.flightNumber() == null || request.flightNumber().isBlank()) {
            throw new ValidationException(ErrorCode.FLIGHT_NUMBER_REQUIRED);
        }
        if (request.initialDepartureTime() == null || request.initialDepartureTime().isBlank()) {
            throw new ValidationException(ErrorCode.INITIAL_DEPARTURE_TIME_REQUIRED);
        }
        if (request.flightHours() == null) {
            throw new ValidationException(ErrorCode.FLIGHT_HOURS_REQUIRED);
        }
        if (request.flightHours() <= 0) {
            throw new ValidationException(ErrorCode.FLIGHT_HOURS_MUST_BE_POSITIVE);
        }
    }

    private FlightInstanceResponse toResponse(FlightInstance flightInstance) {
        return new FlightInstanceResponse(
                flightInstance.flightInstanceId(),
                flightInstance.flightNumber(),
                flightInstance.initialDepartureTime(),
                flightInstance.flightHours()
        );
    }
}
