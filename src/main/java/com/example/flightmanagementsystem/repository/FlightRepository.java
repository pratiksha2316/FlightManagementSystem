package com.example.flightmanagementsystem.repository;

import com.example.flightmanagementsystem.entity.Flight;
import java.util.Optional;

public interface FlightRepository {

    boolean saveIfAbsent(Flight flight);

    Optional<Flight> findByFlightNumber(String flightNumber);

    boolean existsByFlightNumber(String flightNumber);
}
