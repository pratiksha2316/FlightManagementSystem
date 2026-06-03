package com.example.flightmanagementsystem.repository.impl;

import com.example.flightmanagementsystem.entity.Flight;
import com.example.flightmanagementsystem.repository.FlightRepository;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryFlightRepository implements FlightRepository {

    private final ConcurrentMap<String, Flight> flightsByNumber = new ConcurrentHashMap<>();

    @Override
    public boolean saveIfAbsent(Flight flight) {
        return flightsByNumber.putIfAbsent(flight.flightNumber(), flight) == null;
    }

    @Override
    public Optional<Flight> findByFlightNumber(String flightNumber) {
        return Optional.ofNullable(flightsByNumber.get(flightNumber));
    }

    @Override
    public boolean existsByFlightNumber(String flightNumber) {
        return flightsByNumber.containsKey(flightNumber);
    }
}
