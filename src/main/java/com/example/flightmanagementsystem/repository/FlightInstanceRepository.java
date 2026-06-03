package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.entity.FlightInstance;
import java.util.Optional;

public interface FlightInstanceRepository {

    boolean saveIfAbsent(FlightInstance flightInstance);

    Optional<FlightInstance> findByFlightInstanceId(String flightInstanceId);

    boolean existsByFlightInstanceId(String flightInstanceId);
}
